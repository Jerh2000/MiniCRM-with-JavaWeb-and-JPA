package com.controller;

import com.entity.Producto;
import com.services.ProductoServices;
import com.utilidades.FacesUtil;
import com.utilidades.ImageUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Jairo
 */
@ManagedBean
@SessionScoped
public class ProductoController implements Serializable {

    private Producto proimg = new Producto();
    private boolean mostInfoProducto = false;
    private boolean mostRegistroProducto = false;
    private boolean mostBtnRegistrarProducto = false;
    private boolean mostBtnEditarProducto = false;
    private Producto snProducto = new Producto();

    // entidades de negocio
    private Producto producto = new Producto();

    // servicios
    ProductoServices proser = new ProductoServices();

    // colecciones
    private List<Producto> productos = new LinkedList();
    private List<Producto> productosRelacionados = new LinkedList<>();
    private List<Producto> productosVitrina = new LinkedList<>();

    //Objeto para subir imagenes
    private UploadedFile fileimages;

    public ProductoController() {

    }

    //METODO PARA CONSULTAR UN PRODUCTO A MOSTRAR
    public void consultar(Producto p) {
        producto = p;

    }

    //CONSULTA DE UN PRODUCTO A BASE DE DATOS PARA IDENTIFICAR UN PRODUCTO AL CUAL LE ASIGNARE UN IMAGEN
    public void consultarProductoDB(Producto p) {
        proimg = proser.consultaProducto(p.getId());
    }

    //METODO QUE DESHABILITA EL ESTADO DE UN PRODUCTO
    public void deshabilitar(Producto p) {
        p.setEstado("Inactivo");
        p = proser.modificar(p);
        producto = new Producto();
        obtenerProductos();
    }

    //METODO QUE VALIDA QUE LOS CAMPOS DE REGISTRO DE UN PRODUCTO NO ESTEN VACIOS, AUNQUE FALTAN MAS 
    public boolean validar() {

        boolean valido = true;

        if (producto.getNombre().equals("")) {
            valido = false;
        }
        return valido;
    }

    //METODO PARA REGISTRAR PRODUCTO A LA BASE DE DATOS
    public void registrar() {

        boolean snValido = false;

        for (Producto p : productos) {
            if (p.getCodigosn().equals(producto.getCodigosn())) {
                snValido = true;
                break;
            }
        }

        if (snValido) {
            FacesUtil.addWarnMessage("Apreciado empleado, el codigo SN " + producto.getCodigosn() + " ya esta en uso");
        } else {
            if (validar()) {
                String referencia = "";
                referencia = producto.getNombre().substring(0, 2) + producto.getCategoria().substring(0, 2) + producto.getMarca().substring(0, 2) + producto.getColor().substring(0, 2) + producto.getProcesador().substring(0, 2) + producto.getAlmacenamiento().substring(0, 2) + producto.getRam().substring(0, 2) + producto.getSistemaoperativo().substring(0, 2);
                referencia = referencia.toUpperCase();
                producto.setReferencia(referencia);
                getProducto().setEstado("Activo");
                producto = proser.modificar(producto);
                Producto productoNuevo = producto;
                int contadorStock = 0;
                for (Producto pro : productos) {
                    if (pro.getReferencia().equals(producto.getReferencia())) {
                        producto = pro;
                        producto.setStockm(producto.getStockm() + 1);
                        proser.modificar(producto);
                        contadorStock++;
                    }
                }
                productoNuevo.setStockm(contadorStock + 1);
                proser.modificar(productoNuevo);
                upload(producto);
                FacesUtil.addInfoMessage("Se ha registrado el producto " + producto.getNombre() + " exitosamente");
                producto = new Producto();
                obtenerProductos();

            } else {
                FacesUtil.addErrorMessage("Debe llener todos los campos");
            }
        }
    }

    public void editarProducto(Producto p) {
        mostInfoProducto = false;
        mostRegistroProducto = true;
        mostBtnRegistrarProducto = false;
        mostBtnEditarProducto = true;
        producto = p;
        snProducto = p;
    }

    public void registrarCambiosProducto() {
        if (validar()) {
            boolean snValido = false;

            for (Producto p : productos) {
                if (p.getCodigosn().equals(producto.getCodigosn()) && !snProducto.getCodigosn().equals(p.getCodigosn())) {
                    snValido = true;
                    break;
                }
            }
            if (snValido) {
                FacesUtil.addWarnMessage("Apreciado empleado, el codigo SN " + producto.getCodigosn() + " ya esta en uso");
            } else {
                if (fileimages.getSize() > 0) {

                    String referencia = "";
                    referencia = producto.getNombre().substring(0, 2) + producto.getCategoria().substring(0, 2) + producto.getMarca().substring(0, 2) + producto.getColor().substring(0, 2) + producto.getProcesador().substring(0, 2) + producto.getAlmacenamiento().substring(0, 2) + producto.getRam().substring(0, 2) + producto.getSistemaoperativo().substring(0, 2);
                    referencia = referencia.toUpperCase();
                    producto.setReferencia(referencia);
                    producto = proser.modificar(producto);
                    upload(producto);
                    FacesUtil.addInfoMessage("Se ha editado el producto " + producto.getNombre() + " exitosamente");
                    producto = new Producto();
                    obtenerProductos();
                } else {
                    String referencia = "";
                    referencia = producto.getNombre().substring(0, 2) + producto.getCategoria().substring(0, 2) + producto.getMarca().substring(0, 2) + producto.getColor().substring(0, 2) + producto.getProcesador().substring(0, 2) + producto.getAlmacenamiento().substring(0, 2) + producto.getRam().substring(0, 2) + producto.getSistemaoperativo().substring(0, 2);
                    referencia = referencia.toUpperCase();
                    producto.setReferencia(referencia);
                    producto = proser.modificar(producto);
                    FacesUtil.addInfoMessage("Se ha editado el producto " + producto.getNombre() + " exitosamente");
                    producto = new Producto();
                    obtenerProductos();
                }

            }
        }
    }

    //METODO QUE RESETEA EL OBJETO producto PARA REGISTRAR UN NUEVO
    public void cancelarRegistroProducto() {
        mostRegistroProducto = false;
        mostBtnRegistrarProducto = false;
        mostBtnEditarProducto = false;
        producto = new Producto();
    }

    //METODO QUE ME OBTIENE LOS PRODUCTOS DE LA DB Y LOS LISTA EN productos
    public void obtenerProductos() {
        List<Producto> productosD = new LinkedList<>();
        productos = proser.consultarTodo(Producto.class);
        for (Producto pro : productos) {
            if (pro.getEstado().equals("Activo")) {
                productosD.add(pro);
            }
        }
        productos = productosD;
        resetearVariblesDePanel();
    }

    public void obetenerProductosRelacionados(Producto pro) {
        productosRelacionados = new LinkedList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equals(pro.getCategoria())) {
                productosRelacionados.add(p);
            }
        }
    }

    public void mostrarInfoProducto(Producto p) {
        mostInfoProducto = true;
        mostRegistroProducto = false;
        producto = p;
    }

    public void mostrarRegistroProducto() {
        mostRegistroProducto = true;
        mostInfoProducto = false;
        mostBtnRegistrarProducto = true;
        mostBtnEditarProducto = false;
        producto = new Producto();
    }

    public void resetearVariblesDePanel() {
        mostInfoProducto = false;
        mostRegistroProducto = false;

    }

    public void inactivarProducto(Producto p) {
        p.setEstado("Inactivo");
        producto = proser.modificar(p);
        FacesUtil.addInfoMessage("Se ha inactivado el producto " + p.getNombre());
        productos = proser.consultarTodo(Producto.class);
    }

    public void activarProducto(Producto p) {
        p.setEstado("Activo");
        producto = proser.modificar(p);
        FacesUtil.addInfoMessage("Se ha activado el producto " + p.getNombre());
        productos = proser.consultarTodo(Producto.class);
    }

    public void despacharProducto(Producto p) {
        p.setEstado("Vendido");
        proser.modificar(p);
        obtenerProductos();
    }

    //Metodos paraa subir imagen de producto
    public void upload(Producto p) {
        if (fileimages != null) {
            try {
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String path = servletContext.getRealPath("/imag.png");
                path = path.replace("imag.png", "Imagenes\\Productos\\");
                System.out.println(path);
                ImageUtils.copyFile(p.getId() + ".png", fileimages.getInputStream(), path);
                obtenerProductos();
            } catch (IOException ex) {
                Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesUtil.addErrorMessage("no hay archivo seleccionado");
        }
    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * @return the productos
     */
    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * @param productos the productos to set
     */
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public UploadedFile getFileimages() {
        return fileimages;
    }

    public void setFileimages(UploadedFile fileimages) {
        this.fileimages = fileimages;
    }

    public Producto getProimg() {
        return proimg;
    }

    public void setProimg(Producto proimg) {
        this.proimg = proimg;
    }

    public boolean isMostInfoProducto() {
        return mostInfoProducto;
    }

    public void setMostInfoProducto(boolean mostInfoProducto) {
        this.mostInfoProducto = mostInfoProducto;
    }

    public boolean isMostRegistroProducto() {
        return mostRegistroProducto;
    }

    public void setMostRegistroProducto(boolean mostRegistroProducto) {
        this.mostRegistroProducto = mostRegistroProducto;
    }

    public List<Producto> getProductosRelacionados() {
        return productosRelacionados;
    }

    public void setProductosRelacionados(List<Producto> productosRelacionados) {
        this.productosRelacionados = productosRelacionados;
    }

    public List<Producto> getProductosVitrina() {
        return productosVitrina;
    }

    public void setProductosVitrina(List<Producto> productosVitrina) {
        this.productosVitrina = productosVitrina;
    }

    /**
     * @return the mostBtnRegistrarProducto
     */
    public boolean isMostBtnRegistrarProducto() {
        return mostBtnRegistrarProducto;
    }

    /**
     * @param mostBtnRegistrarProducto the mostBtnRegistrarProducto to set
     */
    public void setMostBtnRegistrarProducto(boolean mostBtnRegistrarProducto) {
        this.mostBtnRegistrarProducto = mostBtnRegistrarProducto;
    }

    /**
     * @return the mostBtnEditarProducto
     */
    public boolean isMostBtnEditarProducto() {
        return mostBtnEditarProducto;
    }

    /**
     * @param mostBtnEditarProducto the mostBtnEditarProducto to set
     */
    public void setMostBtnEditarProducto(boolean mostBtnEditarProducto) {
        this.mostBtnEditarProducto = mostBtnEditarProducto;
    }

    /**
     * @return the snProducto
     */
    public Producto getSnProducto() {
        return snProducto;
    }

    /**
     * @param snProducto the snProducto to set
     */
    public void setSnProducto(Producto snProducto) {
        this.snProducto = snProducto;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Cliente;
import com.entity.DetalleVenta;
import com.entity.Empleado;
import com.entity.Producto;
import com.entity.Venta;
import com.services.DetalleVentaServices;
import com.services.EmpleadoServices;
import com.services.VentaServices;
import com.utilidades.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jairo
 */
@ManagedBean
@SessionScoped
public class VentaController implements Serializable {

    private Venta venta = new Venta();
    private double precioVentaIVa;
    private double precioVentaSinIva;
    private int contadorProductosCarrito;
    private int consecutivoVenta;
    private double totalIVA;
    private double subtotal;
    private Cliente cliente = new Cliente();
    private Empleado empleado = new Empleado();
    private DetalleVenta detalleventa = new DetalleVenta();
    private List<Venta> ventas = new LinkedList();
    private List<DetalleVenta> detallesVentas = new LinkedList();
    private List<DetalleVenta> detallesVentasConsulta = new LinkedList();
    private List<Empleado> empleados = new LinkedList<>();

    private DetalleVentaServices detvenser = new DetalleVentaServices();
    private EmpleadoServices empser = new EmpleadoServices();

    private VentaServices venser = new VentaServices();
    private ProductoController procon = new ProductoController();

    private String codigoVenta;

    /**
     * Creates a new instance of VentaController
     */
    public VentaController() {
    }

    public void resetearTodo() {
        detalleventa = new DetalleVenta();
        detallesVentas = new LinkedList();
        detallesVentasConsulta = new LinkedList();
        ventas = new LinkedList();
        precioVentaIVa = 0;
        precioVentaSinIva = 0;
        codigoVenta = "";
    }

    public void obtenerTodasLasVentas() {
        resetearTodo();
        ventas = venser.consultarTodo(Venta.class);
    }

    public void obtenerComprasCliente() {
        obtenerTodasLasVentas();
        consecutivoVenta = ventas.size();
        resetearTodo();
        contarProductoEnCarrito();
        ventas = venser.obtenerComprasCliente(cliente);
    }

    public void obtenerVentasACargoEmpleado() {
        resetearTodo();
        ventas = venser.obtenerComprasEmpleado(empleado);
    }

    public void registrarVentaDeUnProducto(Producto p) {
        if (p.getPromocion() != null) {
            Empleado emp = new Empleado();
            setEmpleados(empser.consultarTodo(Empleado.class));
            int max = getEmpleados().size() - 1;
            int n = (int) (Math.random() * (max - 0)) + 0;
            emp = getEmpleados().get(n);
            
                        venta.setEmpleado(emp);
            venta.setEstado("Por pagar");
            venta.setFecha(new Date());
            venta.setCodigo("VEN" + consecutivoVenta);
            venta.setCliente(cliente);
            venta.setTotalventa(p.getPromocion().getPreciopromocion());
            venta = venser.modificar(venta);

            detalleventa.setCantidad(1);
            detalleventa.setVenta(venta);
            detalleventa.setProducto(p);
            detalleventa = detvenser.modificar(detalleventa);
            procon.despacharProducto(detalleventa.getProducto());
            obtenerComprasCliente();
            FacesUtil.addInfoMessage("Su compra " + venta.getCodigo() + " Se ha enviado para validar");
        } else {
            Empleado emp = new Empleado();
            setEmpleados(empser.consultarTodo(Empleado.class));
            int max = getEmpleados().size() - 1;
            int n = (int) (Math.random() * (max - 0)) + 0;
            emp = getEmpleados().get(n);
           

            venta.setEmpleado(emp);
            venta.setEstado("Por pagar");
            venta.setFecha(new Date());
            venta.setCodigo("VEN" + consecutivoVenta);
            venta.setCliente(cliente);
            venta.setTotalventa(p.getPrecio());
            venta = venser.modificar(venta);

            detalleventa.setCantidad(1);
            detalleventa.setVenta(venta);
            detalleventa.setProducto(p);
            detalleventa = detvenser.modificar(detalleventa);
            procon.despacharProducto(detalleventa.getProducto());
            obtenerComprasCliente();
            FacesUtil.addInfoMessage("Su compra " + venta.getCodigo() + " Se ha enviado para validar");
        }
    }

    public void registrarVentaDeProductos() {
        if (detallesVentas.size() > 0) {
            
            double totalVenta = 0;
            Empleado emp = new Empleado();
            setEmpleados(empser.consultarTodo(Empleado.class));
            int max = getEmpleados().size() - 1;
            int n = (int) (Math.random() * (max - 0)) + 0;
            emp = getEmpleados().get(n);
            int num = ventas.size() + 1;
            for(DetalleVenta de: detallesVentas){
                if(de.getProducto().getPromocion() != null){
                    totalVenta = totalVenta + de.getProducto().getPromocion().getPreciopromocion();
                }else{
                    totalVenta = totalVenta + de.getProducto().getPrecio();
                }
            }

            venta.setEmpleado(emp);
            venta.setEstado("Por pagar");
            venta.setFecha(new Date());
            venta.setCodigo("VEN" + num);
            venta.setCliente(cliente);
            venta.setTotalventa(totalVenta);
            venta = venser.modificar(venta);
            for (DetalleVenta det : detallesVentas) {
                det.setVenta(venta);
                detvenser.crear(det);
            }
            FacesUtil.addInfoMessage("La compra se ha enviado para validar " + venta.getCodigo());
            obtenerComprasCliente();
        } else {
            FacesUtil.addErrorMessage("No hay productos en el carrito");
        }
    }

    //METODO QUE ME PERMITE VALIDAR QUE UN PRODUCTO NO SE VAYA A COMPRAR MAS DE UNA VEZ EN UN COTIZACION
    public boolean existeProductoACarrito(Producto p) {
        boolean existe = false;
        for (DetalleVenta detven : detallesVentas) {
            if (detven.getProducto().getId().equals(p.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /*METODO QUE ME AGREGA A UNA LISTA TEMPORAL (detalleven) LOS DETALLES DE VENTA DE CADA PRODUCTO SELECCIONADO
    /*A COMPRAR*/
    public void agregarProductoACarrito(Producto p) {

        DetalleVenta detalleven = new DetalleVenta();
        detalleven.setProducto(p);
        if (!existeProductoACarrito(p)) {
            detallesVentas.add(detalleven);
            contarProductoEnCarrito();
            FacesUtil.addInfoMessage("Producto " + p.getNombre() + " agregado al carrito");
        } else {
            FacesUtil.addWarnMessage("El producto " + p.getNombre() + " ya esta agregado al carrito");

        }
    }

    public void quitarProductoDeCarrito(DetalleVenta d) {
        detallesVentas.remove(d);
        FacesUtil.addInfoMessage("Producto " + d.getProducto().getNombre() + " quitado del carrito");
        contarProductoEnCarrito();
    }

    public void contarProductoEnCarrito() {
        contadorProductosCarrito = detallesVentas.size();
    }

    public void obtenerDetallesDeCompra(Venta v) {
        detallesVentasConsulta = detvenser.obtenerDetallesCompra(v);
        codigoVenta = v.getCodigo();
        precioVentaSinIva = v.getTotalventa() - v.getTotalventa() * 0.19;
        precioVentaIVa = v.getTotalventa();
    }

    /**
     * @return the venta
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the ventas
     */
    public List<Venta> getVentas() {
        return ventas;
    }

    /**
     * @param ventas the ventas to set
     */
    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    /**
     * @return the detalleventa
     */
    public DetalleVenta getDetalleventa() {
        return detalleventa;
    }

    /**
     * @param detalleventa the detalleventa to set
     */
    public void setDetalleventa(DetalleVenta detalleventa) {
        this.detalleventa = detalleventa;
    }

    /**
     * @return the procon
     */
    public ProductoController getProcon() {
        return procon;
    }

    /**
     * @param procon the procon to set
     */
    public void setProcon(ProductoController procon) {
        this.procon = procon;
    }

    /**
     * @return the detallesVentas
     */
    public List<DetalleVenta> getDetallesVentas() {
        return detallesVentas;
    }

    /**
     * @param detallesVentas the detallesVentas to set
     */
    public void setDetallesVentas(List<DetalleVenta> detallesVentas) {
        this.detallesVentas = detallesVentas;
    }

    /**
     * @return the detallesVentasConsulta
     */
    public List<DetalleVenta> getDetallesVentasConsulta() {
        return detallesVentasConsulta;
    }

    /**
     * @param detallesVentasConsulta the detallesVentasConsulta to set
     */
    public void setDetallesVentasConsulta(List<DetalleVenta> detallesVentasConsulta) {
        this.detallesVentasConsulta = detallesVentasConsulta;
    }

    /**
     * @return the codigoVenta
     */
    public String getCodigoVenta() {
        return codigoVenta;
    }

    /**
     * @param codigoVenta the codigoVenta to set
     */
    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public double getPrecioVentaIVa() {
        return precioVentaIVa;
    }

    public void setPrecioVentaIVa(double precioVentaIVa) {
        this.precioVentaIVa = precioVentaIVa;
    }

    public double getPrecioVentaSinIva() {
        return precioVentaSinIva;
    }

    public void setPrecioVentaSinIva(double precioVentaSinIva) {
        this.precioVentaSinIva = precioVentaSinIva;
    }

    /**
     * @return the contadorProductosCarrito
     */
    public int getContadorProductosCarrito() {
        return contadorProductosCarrito;
    }

    /**
     * @param contadorProductosCarrito the contadorProductosCarrito to set
     */
    public void setContadorProductosCarrito(int contadorProductosCarrito) {
        this.contadorProductosCarrito = contadorProductosCarrito;
    }

    /**
     * @return the totalIVA
     */
    public double getTotalIVA() {
        return totalIVA;
    }

    /**
     * @param totalIVA the totalIVA to set
     */
    public void setTotalIVA(double totalIVA) {
        this.totalIVA = totalIVA;
    }

    /**
     * @return the subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the empleados
     */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /**
     * @param empleados the empleados to set
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    /**
     * @return the consecutivoVenta
     */
    public int getConsecutivoVenta() {
        return consecutivoVenta;
    }

    /**
     * @param consecutivoVenta the consecutivoVenta to set
     */
    public void setConsecutivoVenta(int consecutivoVenta) {
        this.consecutivoVenta = consecutivoVenta;
    }

}

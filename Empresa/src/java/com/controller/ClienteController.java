/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Cliente;
import com.entity.Producto;
import com.services.ClienteServices;
import com.utilidades.FacesUtil;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jairo
 */
@ManagedBean
@SessionScoped
public class ClienteController implements Serializable {

    private String paginaActual = "";
    private String tituloActual = "";

    private Cliente cliente = new Cliente();
    private ClienteServices cliser = new ClienteServices();
    private Producto productoSeleccionado = new Producto();

    @ManagedProperty("#{productoController}")
    private ProductoController productoCon = new ProductoController();
    @ManagedProperty("#{cotizacionController}")
    private CotizacionController coticon = new CotizacionController();
    @ManagedProperty("#{comentarioController}")
    private ComentarioController comcon = new ComentarioController();
    @ManagedProperty("#{ventaController}")
    private VentaController vencon = new VentaController();

    public ClienteController() {
    }

    //VALIDA QUE LOS CAMPOS DE REGISTRO DEL FORMULARIO NO ESTEN VACIOS
    public boolean validarDatosClientes() {
        boolean valido = true;

        if (cliente.getNombre().equals("") || cliente.getApellido().equals("")
                || cliente.getDireccion().equals("") || cliente.getTelefono().equals("")
                || cliente.getIdentificacion().equals("") || cliente.getEmail().equals("")
                || cliente.getLogin().equals("") || cliente.getPassword().equals("")) {
            valido = false;
        }

        return valido;
    }

    public boolean validarDatosClientesModificar(Cliente c) {
        boolean valido = true;

        if (c.getNombre().equals("") || c.getApellido().equals("")
                || c.getDireccion().equals("") || c.getTelefono().equals("")
                || c.getIdentificacion().equals("") || c.getEmail().equals("")) {
            valido = false;
        }

        return valido;
    }

    //REGISTRO DE CLIENTE A BASE DE DATOS TOMANDO OBJETO CLIENTE
    public void registrar() {
        if (validarDatosClientes()) {
            if (cliser.validar(cliente.getLogin())) {
                FacesUtil.addErrorMessage("¡El nombre de usuario " + cliente.getLogin() + " ya existe, intente con otro!");
            } else {
                cliente.setEstado("Activo");
                cliente.setTipo("Cliente");
                getCliser().crear(getCliente());
                setCliente(new Cliente());
                FacesUtil.addInfoMessage("Usuario registrado, por favor ahora inicie sesión");
            }
        } else {
            FacesUtil.addErrorMessage("Estimado cliente, por favor llene todos los campos");
        }
    }

    public Cliente modificar(Cliente c) {
        Cliente clienter = new Cliente();
        if (validarDatosClientesModificar(c)) {

            clienter = getCliser().modificar(c);
            setCliente(new Cliente());
            FacesUtil.addInfoMessage("Usuario modificado con exito");

        } else {
            FacesUtil.addErrorMessage("Estimado cliente, por favor llene todos los campos");
        }
        return clienter;
    }

    //METODO REDIRECCIONAMIENTO A PAGINA DE VITRINA DE PRODUCTOS
    public void verVitrinaProductos() {
        paginaActual = "/Vistas/Cliente/vitrinaProductos.xhtml";
        tituloActual = " - Productos";

    }

    //METODO QUE REDIRECCIONA A PAGINA DE COTIZACIONES DE CLIENTE
    public void verMisCotizaciones() {
        paginaActual = "/Vistas/Cliente/misCotizaciones.xhtml";
        tituloActual = " - Mis cotización";
    }

    public void verPerfilCliente() {
        paginaActual = "/Vistas/Cliente/perfilCliente.xhtml";
    }
    
        
    public void verMisCompras() {
        paginaActual = "/Vistas/Cliente/misCompras.xhtml";
    }

    //METODO QUE REDIRECCIONA A PAGINA DONDE SE AMPLIAN LOS DETALLES DE UN PRODUCTO EN ESPECIFICO
    public void verDetalleProducto(Producto p) {
        paginaActual = "/Vistas/Cliente/detalleProducto.xhtml";
        productoSeleccionado = p;
        productoCon.obetenerProductosRelacionados(p);
        comcon.listarComentariosDeProducto(p);

    }

    public String getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
    }

    public ProductoController getProductoCon() {
        return productoCon;
    }

    public void setProductoCon(ProductoController productoCon) {
        this.productoCon = productoCon;
    }

    public ComentarioController getComcon() {
        return comcon;
    }

    public void setComcon(ComentarioController comcon) {
        this.comcon = comcon;
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
     * @return the cliser
     */
    public ClienteServices getCliser() {
        return cliser;
    }

    /**
     * @param cliser the cliser to set
     */
    public void setCliser(ClienteServices cliser) {
        this.cliser = cliser;
    }

    public CotizacionController getCoticon() {
        return coticon;
    }

    public void setCoticon(CotizacionController coticon) {
        this.coticon = coticon;
    }

    public String getTituloActual() {
        return tituloActual;
    }

    public void setTituloActual(String tituloActual) {
        this.tituloActual = tituloActual;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    /**
     * @return the vencon
     */
    public VentaController getVencon() {
        return vencon;
    }

    /**
     * @param vencon the vencon to set
     */
    public void setVencon(VentaController vencon) {
        this.vencon = vencon;
    }

}

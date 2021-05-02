/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

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
public class AdministradorController implements Serializable {

    /**
     * Creates a new instance of AdministradorController
     */
    private String paginaActual;
    
    
    @ManagedProperty("#{productoController}")
    private ProductoController productoCon = new ProductoController();
    @ManagedProperty("#{promocionController}")
    PromocionController promcon = new PromocionController();
    
     @ManagedProperty("#{ventaController}")
    private VentaController vencon = new VentaController();
    

    public AdministradorController() {
    }
    
    public void verGestionProductos(){
        paginaActual = "/Vistas/Administrador/gestionProducto.xhtml";
    }
    public void verGestionEmpleados(){
        paginaActual = "/Vistas/Administrador/gestionEmpleado.xhtml";
    }
   
    public void verPerfilAdministrador() {
        paginaActual = "/Vistas/Administrador/perfilAdministrador.xhtml";
    }
    public void verVentas(){
        paginaActual = "/Vistas/Administrador/gestionVenta.xhtml";
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

    public PromocionController getPromcon() {
        return promcon;
    }

    public void setPromcon(PromocionController promcon) {
        this.promcon = promcon;
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

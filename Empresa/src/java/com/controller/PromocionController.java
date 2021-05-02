/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Administrador;
import com.entity.Producto;
import com.entity.Promocion;
import com.services.ProductoServices;
import com.services.PromocionServices;
import com.utilidades.FacesUtil;
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
public class PromocionController {

    /**
     * Creates a new instance of PromocionController
     */
    
    private boolean mostRegistrarPromocion = false;
    
    private Promocion promocion = new Promocion();
    private Producto producto = new Producto();
    private Administrador administrador = new Administrador();
    
    private PromocionServices promser = new PromocionServices();
    private ProductoServices proser = new ProductoServices();
    private List<Producto> productos= new LinkedList<>();
    
    public PromocionController() {
    }

    
    public void registrarPromocion(Producto p){
 
        double precioprom = (p.getPrecio() - (p.getPrecio() * (promocion.getDescuento()/100)));
        productos = proser.consultarTodo(Producto.class);
        promocion.setPreciopromocion(precioprom);
        promocion.setEstado("Vigente");
        promocion.setAdministrador(administrador);
        promocion = promser.modificar(promocion);
        for(Producto pro: productos){
            if(pro.getReferencia().equals(p.getReferencia())){
                pro.setPromocion(promocion);
                proser.modificar(pro);
            }
        }
        promocion = new Promocion();
        FacesUtil.addInfoMessage("La promocion ha sido almacenada con exito");
    }
    
    public void cancelarRegistroPromocion(){
        mostRegistrarPromocion = false;
        promocion = new Promocion();
    }
    
    public void mostrarRegistroPromocion(){
        mostRegistrarPromocion = true;
    }

    public boolean isMostRegistrarPromocion() {
        return mostRegistrarPromocion;
    }

    public void setMostRegistrarPromocion(boolean mostRegistrarPromocion) {
        this.mostRegistrarPromocion = mostRegistrarPromocion;
    }
    
    
    
    
    
    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
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
    
}

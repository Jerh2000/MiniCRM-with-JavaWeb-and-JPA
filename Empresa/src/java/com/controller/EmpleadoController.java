/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Empleado;
import com.services.EmpleadoServices;
import com.utilidades.FacesUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jairo
 */
@ManagedBean
@SessionScoped
public class EmpleadoController implements Serializable {

    private String paginaActual = "";
    private boolean mostRegistro = false;
    private boolean mostInfoEmpleado = false;
    private boolean mostModificarEmpleado = false;
    private boolean mostRegistrarEmpleado = false;
    private Empleado empleado = new Empleado();
    private Empleado empleadoValidar = new Empleado();

    List<Empleado> empleados = new LinkedList();

    private EmpleadoServices empleser = new EmpleadoServices();

    @ManagedProperty("#{productoController}")
    private ProductoController prodcon = new ProductoController();

    @ManagedProperty("#{cotizacionController}")
    private CotizacionController coticon = new CotizacionController();

    @ManagedProperty("#{ventaController}")
    private VentaController vencon = new VentaController();

    public EmpleadoController() {
    }

    //METODO DE PRUEBA,NO ES MAS UTILIZADO
    public void generarEmpleado() {
        Empleado emp = new Empleado();
        emp.setApellido("Gonzalez");
        emp.setEmail("jairo@gmail.com");
        emp.setEstado("activo");
        emp.setIdentificacion("23123123");
        emp.setLogin("jairo");
        emp.setNombre("Jairo");
        emp.setPassword("123456");
        emp.setTipo("Empleado");
        EmpleadoServices empser = new EmpleadoServices();
        empser.crear(emp);
    }

    public boolean validarDatosEmpleado() {
        boolean valido = true;

        if (empleado.getNombre().equals("") || empleado.getApellido().equals("")
                || empleado.getIdentificacion().equals("") || empleado.getEmail().equals("")
                || empleado.getLogin().equals("") || empleado.getPassword().equals("")) {
            valido = false;
        }

        return valido;
    }

    public boolean validarDatosEmpleadoE(Empleado e) {
        boolean valido = true;

        if (e.getNombre().equals("") || e.getApellido().equals("")
                || e.getIdentificacion().equals("") || e.getEmail().equals("")) {
            valido = false;
        }

        return valido;
    }

    public void registrarEmpleado() {
        if (validarDatosEmpleado()) {
            if (empleser.validarEmpleado(empleado.getLogin())) {
                FacesUtil.addErrorMessage("¡El nombre de usuario " + empleado.getLogin() + " ya existe, intente con otro!");
            } else {
                empleado.setEstado("Activo");
                empleado.setTipo("Empleado");
                empleser.crear(getEmpleado());
                setEmpleado(new Empleado());
                FacesUtil.addInfoMessage("Empleado registrado exitosamente");
            }
        } else {
            FacesUtil.addErrorMessage("Estimado Administrador, por favor llene todos los campos");
        }
    }

    public void modificarEmpleado() {
        if (validarDatosEmpleado()) {
            if (empleser.validarEmpleado(empleado.getLogin()) && !(empleado.getLogin().equals(empleadoValidar.getLogin()))) {
                FacesUtil.addErrorMessage("¡El nombre de usuario " + empleado.getLogin() + " ya existe, intente con otro!");
            } else {
                empleado.setEstado("Activo");
                empleado.setTipo("Empleado");
                empleser.crear(getEmpleado());
                setEmpleado(new Empleado());
                FacesUtil.addInfoMessage("Cambios guardados exitosamente");
            }
        } else {
            FacesUtil.addErrorMessage("Estimado Administrador, por favor llene todos los campos");
        }
    }

    public Empleado modificarE(Empleado e) {
        Empleado empleador = new Empleado();
        if (validarDatosEmpleadoE(e)) {

            empleador = empleser.modificar(e);
            setEmpleado(new Empleado());
            FacesUtil.addInfoMessage("Usuario modificado con exito");

        } else {
            FacesUtil.addErrorMessage("Estimado cliente, por favor llene todos los campos");
        }
        return empleador;
    }

    public void obtenerEmpleados() {
        empleados = empleser.consultarTodo(Empleado.class);
    }

    public void editarEmpleado(Empleado e) {
        mostRegistro = true;
        mostInfoEmpleado = false;
        mostRegistrarEmpleado = false;
        mostModificarEmpleado = true;
        empleado = e;
        empleadoValidar = e;
    }

    public void nuevoEmpleado() {
        mostRegistro = true;
        mostRegistrarEmpleado = true;
        mostInfoEmpleado = false;
        mostModificarEmpleado = false;
        empleado = new Empleado();
    }

    public void cancelarRegistroEmpleado() {
        empleado = new Empleado();
        mostRegistro = false;
        mostRegistrarEmpleado = false;
    }

    public void consultarInfoEmpleado(Empleado e) {
        mostInfoEmpleado = true;
        mostRegistro = false;
        empleado = e;

    }

    public void inactivarCuentaEmpleado(Empleado e) {
        e.setEstado("Inactivo");
        empleado = empleser.modificar(e);
        FacesUtil.addInfoMessage("La cuenta del empleado " + empleado.getNombre() + " ha sido inactivada");
        obtenerEmpleados();
    }

    public void activarCuentaEmpleado(Empleado e) {
        e.setEstado("Activo");
        empleado = empleser.modificar(e);
        FacesUtil.addInfoMessage("La cuenta del empleado " + empleado.getNombre() + " ha sido Activada");
        obtenerEmpleados();
    }

    //METODO QUE ME REDIRIGE A LA VISTA DE GESTION DE PRODUCTOS DONDE SE REGISTRAN DESDE LA SESION DE UN EMPLEADO
    public void mostrarMenuProductos() {
        paginaActual = "/Vistas/Empleado/gestionProductos.xhtml";
    }

    /*METODO QUE ME REDIRIGE A LA VISTA DE GESTION DE COTIZACIONES DONDE SE PUEDEN OBSERVER LAS COTIZACIONES
    /*QUE HAN SOLICITADO LOS CLIENTES DESDE LA SESION DE UN CLIENTE*/
    public void mostrarMenuCotizaciones() {
        paginaActual = "/Vistas/Empleado/gestionCotizaciones.xhtml";
    }
    
    public void verGestionVentas() {
        paginaActual = "/Vistas/Empleado/gestionVenta.xhtml";
    }

    public void verPerfilEmpleado() {
        paginaActual = "/Vistas/Empleado/perfilEmpleado.xhtml";
    }

    public void reseterarVariablesDeControlDePanel() {
        mostInfoEmpleado = false;
        mostModificarEmpleado = false;
        mostRegistrarEmpleado = false;
        mostRegistro = false;
    }

    public String getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Empleado getEmpleadoValidar() {
        return empleadoValidar;
    }

    public void setEmpleadoValidar(Empleado empleadoValidar) {
        this.empleadoValidar = empleadoValidar;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public ProductoController getProdcon() {
        return prodcon;
    }

    public void setProdcon(ProductoController prodcon) {
        this.prodcon = prodcon;
    }

    public CotizacionController getCoticon() {
        return coticon;
    }

    public void setCoticon(CotizacionController coticon) {
        this.coticon = coticon;
    }

    public boolean isMostRegistro() {
        return mostRegistro;
    }

    public void setMostRegistro(boolean mostRegistro) {
        this.mostRegistro = mostRegistro;
    }

    public boolean isMostInfoEmpleado() {
        return mostInfoEmpleado;
    }

    public void setMostInfoEmpleado(boolean mostInfoEmpleado) {
        this.mostInfoEmpleado = mostInfoEmpleado;
    }

    public boolean isMostModificarEmpleado() {
        return mostModificarEmpleado;
    }

    public void setMostModificarEmpleado(boolean mostModificarEmpleado) {
        this.mostModificarEmpleado = mostModificarEmpleado;
    }

    public boolean isMostRegistrarEmpleado() {
        return mostRegistrarEmpleado;
    }

    public void setMostRegistrarEmpleado(boolean mostRegistrarEmpleado) {
        this.mostRegistrarEmpleado = mostRegistrarEmpleado;
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

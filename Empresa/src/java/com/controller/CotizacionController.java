/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Cliente;
import com.entity.Cotizacion;
import com.entity.DetalleCotizacion;
import com.entity.Empleado;
import com.entity.Producto;
import com.services.CotizacionServices;
import com.services.DetalleCotizacionServices;
import com.services.EmpleadoServices;
import com.utilidades.FacesUtil;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jairo
 */
@ManagedBean
@SessionScoped
public class CotizacionController implements Serializable {

    private double subTotal;
    private double subTotalPropuesto;
    private double totalIVA;
    private double totalPropuestoIVA;
    private int contadorCotizacion;
    private String codigoCotizacion;

    //Variables de renderizado de paneles
    private boolean mostCotizaciones = true;
    private boolean mostCotizacionesS = false;
    private boolean mostCotizacionesA = false;
    private boolean mostCotizacionesR = false;
    private String tipoCotizacion = "Todas";

    private Cliente cliente = new Cliente();
    private Empleado empleado = new Empleado();
    private Cotizacion cotizacion = new Cotizacion();
    private Cotizacion cotizacionConsulta = new Cotizacion();

    private List<DetalleCotizacion> detcotizaciones = new LinkedList();//LISTA DETALLES DE COTIZACIONES QUE LUEGO SE GUARDAN EN BD
    private List<Cotizacion> cotizacionesAprobadas = new LinkedList<>();
    private List<Cotizacion> cotizacionesRechazadas = new LinkedList<>();
    private List<Cotizacion> cotizacionesSolicitadas = new LinkedList<>();
    private List<Cotizacion> cotizacionesCliente = new LinkedList<>();
    private List<Cotizacion> cotizaciones = new LinkedList<>();
    private List<DetalleCotizacion> detcotizacionConsulta = new LinkedList<>();//LISTA LOS DETALLES DE COTIZACIONES PARA EL CLIENTE
    private List<Empleado> empleados = new LinkedList<>();

    private CotizacionServices cotiser = new CotizacionServices();
    private DetalleCotizacionServices detser = new DetalleCotizacionServices();
    private EmpleadoServices empser = new EmpleadoServices();

    public CotizacionController() {
    }

    //METODO QUE ME PERMITE VALIDAR QUE UN PRODUCTO NO SE VAYA A COTIZAR MAS DE UNA VEZ EN UN COTIZACION
    public boolean existeProductoACotizacion(Producto p) {
        boolean existe = false;
        for (DetalleCotizacion decot : detcotizaciones) {
            if (decot.getProducto().getId().equals(p.getId())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /*METODO QUE ME AGREGA A UNA LISTA TEMPORAL (detcotizaciones) LOS DETALLES DE COTIZACION DE CADA PRODUCTO SELECCIONADO
    /*A COTIZAR*/
    public void agregarProductoACotizacion(Producto p) {

        DetalleCotizacion detallecot = new DetalleCotizacion();
        detallecot.setProducto(p);
        if (!existeProductoACotizacion(p)) {
            detcotizaciones.add(detallecot);
            contarCotizacion();
            FacesUtil.addInfoMessage("Producto " + p.getNombre() + " agregado a cotizar");
        } else {
            FacesUtil.addWarnMessage("El producto " + p.getNombre() + " ya esta agregado");

        }
    }

    //NO RECUERDO SI LO USE, CREO QUE NO
    public void agregarProductoACotizacionDesdeDetalle(Producto p) {
        DetalleCotizacion detallecot = new DetalleCotizacion();

        detallecot.setProducto(p);
        if (!existeProductoACotizacion(p)) {
            detcotizaciones.add(detallecot);
            contarCotizacion();
            FacesUtil.addInfoMessage("Producto " + p.getNombre() + " agregado a cotizar");
        } else {
            FacesUtil.addWarnMessage("El producto " + p.getNombre() + " ya esta agregado");

        }
    }

    /*METODO QUE ME PERMITE QUITAR DE LA LISTA (detcotizaciones) UN DETALLE DE COTIZACION QUE EL CLIENTE
    /*YA NO QUIERA COTIZAR*/
    public void quitarProductoDeCotizacion(DetalleCotizacion d) {
        detcotizaciones.remove(d);
        FacesUtil.addInfoMessage("Producto " + d.getProducto().getNombre() + " quitado de la cotización");
        contarCotizacion();
    }

    //METODO QUE ME PERMITE ALMACENAR UNA COTIZACION Y SU DETALLE EN LA BASE DE DATOS
    public void registrarCotizaciones() {

        if (detcotizaciones.size() > 0) {
            Empleado emp = new Empleado();
            empleados = empser.consultarTodo(Empleado.class);
            int max = empleados.size() - 1;
            int n = (int) (Math.random() * (max - 0)) + 0;
            emp = empleados.get(n);
            cotizacion.setEmpleado(emp);
            cotizacion.setCliente(cliente);
            cotizacion.setEstado("Solicitado");
            cotizacion.setFecha(new Date());
            cotizacion.setCodigo("123");
            cotizacion = cotiser.modificar(cotizacion);
            for (DetalleCotizacion decot : detcotizaciones) {
                decot.setCotizacion(cotizacion);
                detser.crear(decot);
            }
            FacesUtil.addInfoMessage("La cotización se ha guardado y enviado satifactoriamente" + cotizacion.getCodigo());
            obtenerCotizacionesCliente();

        } else {
            FacesUtil.addErrorMessage("No hay cotizaciones");
        }
    }

    public int numeroCotizacion() {
        Random rnd = new Random();
        return (int) rnd.nextDouble();
    }

    //METODO QUE ME PERMITE OBTENER TODAS LAS COTIZACIONES DESDE LA SESION DE UN EMPLEADO
    public void obtenerCotizaciones() {
        resetearValores();
        cotizacion = new Cotizacion();
        detcotizaciones = new LinkedList();
        cotizaciones = cotiser.obtenerCotizacionesEmpleado(empleado);
        obtenerCotizacionesSolicitadas();
        obtenerCotizacionesAprobadas();
        obtenerCotizacionesRechazadas();
    }

    //METODO QUE ME OBTIENE SOLO COTIZACIONES EN ESTADO SOLICITADO
    public void obtenerCotizacionesSolicitadas() {
        cotizacionesSolicitadas = new LinkedList<>();
        for (Cotizacion c : cotizaciones) {
            if (c.getEstado().equals("Solicitado")) {
                cotizacionesSolicitadas.add(c);
            }
        }

    }

    //METODO QUE ME OBTIENE SOLO COTIZACIONES EN ESTADO APROBADO
    public void obtenerCotizacionesAprobadas() {
        cotizacionesAprobadas = new LinkedList<>();
        for (Cotizacion c : cotizaciones) {
            if (c.getEstado().equals("Aprobado")) {
                cotizacionesAprobadas.add(c);
            }
        }

    }

    //METODO QUE ME OBTIENE SOLO COTIZACIONES EN ESTADO RECHAZADO
    public void obtenerCotizacionesRechazadas() {
        cotizacionesRechazadas = new LinkedList<>();
        for (Cotizacion c : cotizaciones) {
            if (c.getEstado().equals("Rechazado")) {
                cotizacionesRechazadas.add(c);
            }
        }

    }

    //METODO QUE ME OBTIENE LAS COTIZACIONES DEL CLIENTE QUE ESTA EN SESION
    public void obtenerCotizacionesCliente() {
        cotizacion = new Cotizacion();
        detcotizacionConsulta = new LinkedList<>();
        detcotizaciones = new LinkedList();
        contarCotizacion();
        resetearValores();
        cotizaciones = cotiser.obtenerCotizacionesCliente(cliente);
        obtenerCotizacionesAprobadas();
        obtenerCotizacionesSolicitadas();
        obtenerCotizacionesRechazadas();
    }

    //METODO QUE ME OBTIENE LOS DETALLES DE COTIZACION DE CADA COTIZACION DEL CLIENTE QUE ESTA EN SESION
    public void obtenerDetalleCotizacion(Cotizacion c) {
        resetearValores();
        detcotizacionConsulta = detser.obtenerDetallesCotizacion(c);
        //cotizacion = c;
        calcularValores(detcotizacionConsulta);
        codigoCotizacion = c.getCodigo();
    }

    //METODO QUE ME OBTIENE TODOS LOS DETALLES DE COTIZACION DE CADA COTIZACION PARA QUE EL EMPLEADO EN SESION LAS VISUALICE
    public void obtenerDetalleCotizacionEmpleado(Cotizacion c) {

        resetearValores();
        detcotizaciones = detser.obtenerDetallesCotizacion(c);
        cotizacion = c;
        calcularValores(detcotizaciones);
        codigoCotizacion = c.getCodigo();
    }

    //METODO QUE ME PERMITE APROBAR UNA COTIZACION, SOLO LO PUEDE HACER EL EMPLEADO EN SESION
    public void aprobarCotizacion() {
        if (cotizacion.getId() != null) {
            cotizacion.setEstado("Aprobado");
            cotizacion.setEmpleado(empleado);
            cotizacion = cotiser.modificar(cotizacion);
            detcotizaciones = new LinkedList<>();
            cotizacion = new Cotizacion();
            obtenerCotizaciones();
            FacesUtil.addInfoMessage("La cotización ha sido aprobada");
        } else {
            FacesUtil.addWarnMessage("Seleccione una cotización");
        }
    }

    //METODO QUE ME PERMITE RECHAZAR UNA COTIZACION, SOLO LO PUEDE HACER EL EMPLEADO EN SESION
    public void rechazarCotizacion() {
        if (cotizacion.getId() != null) {
            cotizacion.setEstado("Rechazado");
            cotizacion.setEmpleado(empleado);
            cotizacion = cotiser.modificar(cotizacion);
            cotizacion = new Cotizacion();
            detcotizaciones = new LinkedList<>();
            obtenerCotizaciones();
            FacesUtil.addInfoMessage("La cotización ha sido rechazada");
        } else {
            FacesUtil.addWarnMessage("Seleccione una cotización");
        }
    }

    //METODO QUE ME PERMITE CALCULAR LOS VALORES DE LOS DETALLES DE UNA COTIZACION
    public void calcularValores(List<DetalleCotizacion> detaller) {
        for (DetalleCotizacion d : detaller) {
            subTotalPropuesto = subTotalPropuesto + d.getPropuesta();
            subTotal = subTotal + d.subtotal();
        }
        totalIVA = subTotal * 0.19 + subTotal;
        totalPropuestoIVA = subTotalPropuesto * 0.19 + subTotalPropuesto;
    }

    //METODO QUE ME RESETEA LOS VALORES DE LOS DETALLES DE COTIZACIONES PARA QUE NO SE ACUMULEN
    public void resetearValores() {
        totalIVA = 0;
        totalPropuestoIVA = 0;
        subTotal = 0;
        subTotalPropuesto = 0;
        codigoCotizacion = "";
    }

    //METODO QUE ME PERMITE CONTAR LA CANTIDAD DE DETALLES CON SUS PRODUCTOS DE UNA COTIZACION, SE VE EN EL CARRITO
    public void contarCotizacion() {
        contadorCotizacion = detcotizaciones.size();
    }

    //LOS SIGUIENTES METODOS ME PERMITEN RENDERIZAR PANELES MEDIANTE VARIABLE BOOLEANAS
    public void mostrarTodasCotizaciones() {
        mostCotizacionesS = false;
        mostCotizacionesA = false;
        mostCotizacionesR = false;
        mostCotizaciones = true;
        detcotizacionConsulta = new LinkedList<>();
        detcotizaciones = new LinkedList<>();
        cotizacion = new Cotizacion();
        resetearValores();
        tipoCotizacion = "Todas";
    }

    public void mostratCotizacionesSolicitadas() {
        mostCotizacionesS = true;
        mostCotizacionesA = false;
        mostCotizacionesR = false;
        mostCotizaciones = false;
        detcotizacionConsulta = new LinkedList<>();
        detcotizaciones = new LinkedList<>();
        cotizacion = new Cotizacion();
        resetearValores();
        tipoCotizacion = "Cotizaciones solicitadas";
    }

    public void mostratCotizacionesAprobadas() {
        mostCotizacionesS = false;
        mostCotizacionesA = true;
        mostCotizacionesR = false;
        mostCotizaciones = false;
        detcotizacionConsulta = new LinkedList<>();
        detcotizaciones = new LinkedList<>();
        cotizacion = new Cotizacion();
        resetearValores();
        tipoCotizacion = "Cotizaciones aprobadas";
    }

    public void mostratCotizacionesRechazadas() {
        mostCotizacionesS = false;
        mostCotizacionesA = false;
        mostCotizacionesR = true;
        mostCotizaciones = false;
        detcotizacionConsulta = new LinkedList<>();
        detcotizaciones = new LinkedList<>();
        cotizacion = new Cotizacion();
        resetearValores();
        tipoCotizacion = "Cotizaciones rechazadas";
    }

//Getters and Setter
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public List<DetalleCotizacion> getDetcotizaciones() {
        return detcotizaciones;
    }

    public void setDetcotizaciones(List<DetalleCotizacion> detcotizaciones) {
        this.detcotizaciones = detcotizaciones;
    }

    public List<Cotizacion> getCotizacionesAprobadas() {
        return cotizacionesAprobadas;
    }

    public void setCotizacionesAprobadas(List<Cotizacion> cotizacionesAprobadas) {
        this.cotizacionesAprobadas = cotizacionesAprobadas;
    }

    public List<Cotizacion> getCotizacionesRechazadas() {
        return cotizacionesRechazadas;
    }

    public void setCotizacionesRechazadas(List<Cotizacion> cotizacionesRechazadas) {
        this.cotizacionesRechazadas = cotizacionesRechazadas;
    }

    public List<Cotizacion> getCotizacionesSolicitadas() {
        return cotizacionesSolicitadas;
    }

    public void setCotizacionesSolicitadas(List<Cotizacion> cotizacionesSolicitadas) {
        this.cotizacionesSolicitadas = cotizacionesSolicitadas;
    }

    public List<Cotizacion> getCotizacionesCliente() {
        return cotizacionesCliente;
    }

    public void setCotizacionesCliente(List<Cotizacion> cotizacionesCliente) {
        this.cotizacionesCliente = cotizacionesCliente;
    }

    public List<Cotizacion> getCotizaciones() {
        return cotizaciones;
    }

    public void setCotizaciones(List<Cotizacion> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    public Cotizacion getCotizacionConsulta() {
        return cotizacionConsulta;
    }

    public void setCotizacionConsulta(Cotizacion cotizacionConsulta) {
        this.cotizacionConsulta = cotizacionConsulta;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getSubTotalPropuesto() {
        return subTotalPropuesto;
    }

    public void setSubTotalPropuesto(double subTotalPropuesto) {
        this.subTotalPropuesto = subTotalPropuesto;
    }

    public double getTotalIVA() {
        return totalIVA;
    }

    public void setTotalIVA(double totalIVA) {
        this.totalIVA = totalIVA;
    }

    public double getTotalPropuestoIVA() {
        return totalPropuestoIVA;
    }

    public void setTotalPropuestoIVA(double totalPropuestoIVA) {
        this.totalPropuestoIVA = totalPropuestoIVA;
    }

    public int getContadorCotizacion() {
        return contadorCotizacion;
    }

    public void setContadorCotizacion(int contadorCotizacion) {
        this.contadorCotizacion = contadorCotizacion;
    }

    public List<DetalleCotizacion> getDetcotizacionConsulta() {
        return detcotizacionConsulta;
    }

    public void setDetcotizacionConsulta(List<DetalleCotizacion> detcotizacionConsulta) {
        this.detcotizacionConsulta = detcotizacionConsulta;
    }

    public boolean isMostCotizaciones() {
        return mostCotizaciones;
    }

    public void setMostCotizaciones(boolean mostCotizaciones) {
        this.mostCotizaciones = mostCotizaciones;
    }

    /**
     * @return the mostCotizacionesS
     */
    public boolean isMostCotizacionesS() {
        return mostCotizacionesS;
    }

    /**
     * @param mostCotizacionesS the mostCotizacionesS to set
     */
    public void setMostCotizacionesS(boolean mostCotizacionesS) {
        this.mostCotizacionesS = mostCotizacionesS;
    }

    /**
     * @return the mostCotizacionesA
     */
    public boolean isMostCotizacionesA() {
        return mostCotizacionesA;
    }

    /**
     * @param mostCotizacionesA the mostCotizacionesA to set
     */
    public void setMostCotizacionesA(boolean mostCotizacionesA) {
        this.mostCotizacionesA = mostCotizacionesA;
    }

    /**
     * @return the mostCotizacionesR
     */
    public boolean isMostCotizacionesR() {
        return mostCotizacionesR;
    }

    /**
     * @param mostCotizacionesR the mostCotizacionesR to set
     */
    public void setMostCotizacionesR(boolean mostCotizacionesR) {
        this.mostCotizacionesR = mostCotizacionesR;
    }

    /**
     * @return the codigoCotizacion
     */
    public String getCodigoCotizacion() {
        return codigoCotizacion;
    }

    /**
     * @param codigoCotizacion the codigoCotizacion to set
     */
    public void setCodigoCotizacion(String codigoCotizacion) {
        this.codigoCotizacion = codigoCotizacion;
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
     * @return the tipoCotizacion
     */
    public String getTipoCotizacion() {
        return tipoCotizacion;
    }

    /**
     * @param tipoCotizacion the tipoCotizacion to set
     */
    public void setTipoCotizacion(String tipoCotizacion) {
        this.tipoCotizacion = tipoCotizacion;
    }

}

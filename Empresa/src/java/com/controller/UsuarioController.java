package com.controller;

import com.entity.Administrador;
import com.entity.Cliente;
import com.entity.Empleado;
import com.entity.Usuario;
import com.services.AdministradorServices;
import com.services.ClienteServices;
import com.services.EmpleadoServices;
import com.services.UsuarioServices;
import com.utilidades.FacesUtil;
import com.utilidades.ImageUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
public class UsuarioController implements Serializable {

    private Usuario usuario = new Usuario();
    private Empleado empleado = new Empleado();
    private Empleado empleadoEditar = new Empleado();
    private Cliente cliente = new Cliente();
    private Cliente clienteEditar = new Cliente();
    private Administrador administrador = new Administrador();
    private Administrador administradorEditar = new Administrador();
    private boolean mostpanel = false;
    private String paginaActual = "";
    private UploadedFile fileimages;
    private String contrasenaActual;
    private String nuevaContrasena;
    private String repetirNuevaContrasena;

    private boolean mostEditarUsuario = false;
    private boolean mostEditarContrasena = false;
    private boolean mostBanner = true;

    UsuarioServices ususer = new UsuarioServices();
    ClienteServices cliser = new ClienteServices();
    EmpleadoServices empser = new EmpleadoServices();
    AdministradorServices adminser = new AdministradorServices();

    @ManagedProperty("#{empleadoController}")
    private EmpleadoController emplecon = new EmpleadoController();

    @ManagedProperty("#{clienteController}")
    private ClienteController clientecon = new ClienteController();

    @ManagedProperty("#{administradorController}")
    private AdministradorController admincon = new AdministradorController();

    @ManagedProperty("#{comentarioController}")
    private ComentarioController comencon = new ComentarioController();
    
    @ManagedProperty("#{ventaController}")
    private VentaController vencon = new VentaController();
            

    /**
     * Creates a new instance of UsuarioController
     */
    public UsuarioController() {
    }

    //METODO QUE CIERRA LA SESION DE CADA USUARIO
    public void cerrar() {
        usuario = new Usuario();;
        empleado = null;
        cliente = null;
        administrador = null;
        mostpanel = false;
    }

    //METODO DE INICIO DE SESION DE USUARIO
    public void iniciar() {

        if (usuario.getLogin().equals("") || usuario.getPassword().equals("")) {
            FacesUtil.addWarnMessage("Por favor llene los campos");
        } else {

            usuario = ususer.ingresar(usuario.getLogin(), usuario.getPassword());

            if (usuario.getId() != null) {
                if (usuario.getEstado().equals("Activo")) {
                    //INICIO DE SESION CLIENTE
                    mostpanel = true;
                    if (usuario.getTipo().equals("Cliente")) {
                        setCliente(cliser.consultar(Cliente.class, usuario.getId()));
                        setClienteEditar(cliser.consultar(Cliente.class, usuario.getId()));
                        paginaActual = "Vistas/Cliente/GUICliente.xhtml";
                        clientecon.getCoticon().setCliente(cliente);
                        clientecon.getComcon().setCliente(cliente);
                        clientecon.getVencon().setCliente(cliente);
                        
                        clientecon.getVencon().obtenerComprasCliente();
                        clientecon.getProductoCon().obtenerProductos();
                        comencon.consultarComentarios();
                        clientecon.getCoticon().obtenerCotizacionesCliente();
                    }
                    //INICIO DE SESION EMPLEADO
                    if (usuario.getTipo().equals("Empleado")) {
                        setEmpleado(empser.consultar(Empleado.class, usuario.getId()));
                        setEmpleadoEditar(empser.consultar(Empleado.class, usuario.getId()));
                        emplecon.getCoticon().setEmpleado(empleado);
                        emplecon.getVencon().setEmpleado(empleado);
                        emplecon.getVencon().obtenerVentasACargoEmpleado();
                        emplecon.getProdcon().obtenerProductos();
                        emplecon.getCoticon().obtenerCotizaciones();
                        paginaActual = "Vistas/Empleado/GUIEmpleado.xhtml";

                    }
                    //INICIO DE SEION DE ADMINISTRADOR
                    if (usuario.getTipo().equals("Administrador")) {
                        setAdministrador(adminser.consultar(Administrador.class, usuario.getId()));
                        setAdministradorEditar(adminser.consultar(Administrador.class, usuario.getId()));
                        emplecon.obtenerEmpleados();
                        emplecon.reseterarVariablesDeControlDePanel();
                        admincon.getProductoCon().obtenerProductos();
                        admincon.getPromcon().setAdministrador(administrador);
                        admincon.getVencon().obtenerTodasLasVentas();
                        paginaActual = "Vistas/Administrador/GUIAdministrador.xhtml";
                    }
                } else {
                    FacesUtil.addErrorMessage("Usuario Inactivo. Comuniquese con el administrador para solucionarlo.");
                    mostpanel = false;
                }
            }
        }
    }

    //METODO QUE ME REDIRIGE A LA VISTA DE REGISTRO DE CLIENTE
    public void registrar() {
        mostpanel = true;
        paginaActual = "/Vistas/Cliente/GestionCliente.xhtml";
    }

    public void mostrarEditarUsuario() {
        mostEditarUsuario = true;
        mostEditarContrasena = false;
        mostBanner = false;
    }

    public void guardarCambiosDeUsuario() {

        cliente = clientecon.modificar(clienteEditar);
        mostEditarUsuario = false;
        mostEditarContrasena = false;
        mostBanner = true;

    }

    public void guardarCambiosDeUsuarioE() {
        empleado = emplecon.modificarE(empleadoEditar);
        mostEditarUsuario = false;
        mostEditarContrasena = false;
        mostBanner = true;

    }

    public void mostrarEditarContrasena() {
        mostEditarContrasena = true;
        mostEditarUsuario = false;
        mostBanner = false;
    }

    public void guardarNuevaContrasena() {
        if (nuevaContrasena.equals(repetirNuevaContrasena)) {
            if (contrasenaActual.equals(usuario.getPassword())) {
                usuario.setPassword(nuevaContrasena);
                usuario = ususer.modificar(usuario);
                FacesUtil.addInfoMessage("La constraseña se ha cambiado exitosamente");
                mostEditarUsuario = false;
                mostEditarContrasena = false;
                mostBanner = true;

            } else {
                FacesUtil.addWarnMessage("Contraseña incorrecta");
            }
        } else {
            FacesUtil.addErrorMessage("Las contraseñas nuevas no coinciden");
        }
    }

    public void cancelarEditarUsuario() {
        mostEditarUsuario = false;
        mostEditarContrasena = false;
        mostBanner = true;
    }

    public void upload(Usuario u) {
        if (getFileimages() != null) {
            System.out.println("Imagen " + fileimages);
            try {
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String path = servletContext.getRealPath("/imag.png");
                path = path.replace("imag.png", "Imagenes\\FotosUsuario\\");
                System.out.println(path);
                ImageUtils.copyFile(u.getId() + ".png", getFileimages().getInputStream(), path);
                FacesUtil.addInfoMessage("Su foto ha sido subida con exito");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesUtil.addErrorMessage("no hay archivo seleccionado");
        }
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the mostpanel
     */
    public boolean isMostpanel() {
        return mostpanel;
    }

    /**
     * @param mostpanel the mostpanel to set
     */
    public void setMostpanel(boolean mostpanel) {
        this.mostpanel = mostpanel;
    }

    /**
     * @return the paginaActual
     */
    public String getPaginaActual() {
        return paginaActual;
    }

    /**
     * @param paginaActual the paginaActual to set
     */
    public void setPaginaActual(String paginaActual) {
        this.paginaActual = paginaActual;
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

    public ClienteController getClientecon() {
        return clientecon;
    }

    public void setClientecon(ClienteController clientecon) {
        this.clientecon = clientecon;
    }

    public EmpleadoController getEmplecon() {
        return emplecon;
    }

    public void setEmplecon(EmpleadoController emplecon) {
        this.emplecon = emplecon;
    }

    public ComentarioController getComencon() {
        return comencon;
    }

    public void setComencon(ComentarioController comencon) {
        this.comencon = comencon;
    }

    /**
     * @return the administrador
     */
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * @param administrador the administrador to set
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public AdministradorController getAdmincon() {
        return admincon;
    }

    public void setAdmincon(AdministradorController admincon) {
        this.admincon = admincon;
    }

    /**
     * @return the fileimages
     */
    public UploadedFile getFileimages() {
        return fileimages;
    }

    /**
     * @param fileimages the fileimages to set
     */
    public void setFileimages(UploadedFile fileimages) {
        this.fileimages = fileimages;
    }

    /**
     * @return the mostEditarUsuario
     */
    public boolean isMostEditarUsuario() {
        return mostEditarUsuario;
    }

    /**
     * @param mostEditarUsuario the mostEditarUsuario to set
     */
    public void setMostEditarUsuario(boolean mostEditarUsuario) {
        this.mostEditarUsuario = mostEditarUsuario;
    }

    /**
     * @return the nuevaContrasena
     */
    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    /**
     * @param nuevaContrasena the nuevaContrasena to set
     */
    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    /**
     * @return the repetirNuevaContrasena
     */
    public String getRepetirNuevaContrasena() {
        return repetirNuevaContrasena;
    }

    /**
     * @param repetirNuevaContrasena the repetirNuevaContrasena to set
     */
    public void setRepetirNuevaContrasena(String repetirNuevaContrasena) {
        this.repetirNuevaContrasena = repetirNuevaContrasena;
    }

    /**
     * @return the mostEditarContrasena
     */
    public boolean isMostEditarContrasena() {
        return mostEditarContrasena;
    }

    /**
     * @param mostEditarContrasena the mostEditarContrasena to set
     */
    public void setMostEditarContrasena(boolean mostEditarContrasena) {
        this.mostEditarContrasena = mostEditarContrasena;
    }

    /**
     * @return the contrasenaActual
     */
    public String getContrasenaActual() {
        return contrasenaActual;
    }

    /**
     * @param contrasenaActual the contrasenaActual to set
     */
    public void setContrasenaActual(String contrasenaActual) {
        this.contrasenaActual = contrasenaActual;
    }

    /**
     * @return the clienteEditar
     */
    public Cliente getClienteEditar() {
        return clienteEditar;
    }

    /**
     * @param clienteEditar the clienteEditar to set
     */
    public void setClienteEditar(Cliente clienteEditar) {
        this.clienteEditar = clienteEditar;
    }

    /**
     * @return the mostBanner
     */
    public boolean isMostBanner() {
        return mostBanner;
    }

    /**
     * @param mostBanner the mostBanner to set
     */
    public void setMostBanner(boolean mostBanner) {
        this.mostBanner = mostBanner;
    }

    /**
     * @return the empleadoEditar
     */
    public Empleado getEmpleadoEditar() {
        return empleadoEditar;
    }

    /**
     * @param empleadoEditar the empleadoEditar to set
     */
    public void setEmpleadoEditar(Empleado empleadoEditar) {
        this.empleadoEditar = empleadoEditar;
    }

    /**
     * @return the administradorEditar
     */
    public Administrador getAdministradorEditar() {
        return administradorEditar;
    }

    /**
     * @param administradorEditar the administradorEditar to set
     */
    public void setAdministradorEditar(Administrador administradorEditar) {
        this.administradorEditar = administradorEditar;
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

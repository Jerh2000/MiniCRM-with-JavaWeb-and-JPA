/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Cliente;
import com.entity.Comentario;
import com.entity.Producto;
import com.services.ComentarioService;
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
public class ComentarioController implements Serializable {

    private double valoracionProductoDouble;
    private int valoracionProducto;
    private int starOne, starTwo, starThree, starFour, starFive;

    private Comentario comentario = new Comentario();
    private Cliente cliente = new Cliente();

    private List<Comentario> comentarios = new LinkedList<>();
    private List<Comentario> comentariosDeProducto = new LinkedList<>();

    private ComentarioService comenserv = new ComentarioService();

    public ComentarioController() {
    }

    //METODO QUE ME GUARDA EL COMENTARIO CON SUL VALORACION DE UN PRODUCTO EN ESPECIFICO EN LA BD
    public void guardarComentario(Producto p) {
        if (comentario.getDescripcion().equals("")) {
            FacesUtil.addWarnMessage("Por favor escriba el comentario");
        } else {
            comentario.setFecha(new Date());
            comentario.setCliente(cliente);
            comentario.setProducto(p);
            comenserv.crear(comentario);
            consultarComentarios();
            listarComentariosDeProducto(p);
            comentario = new Comentario();
            FacesUtil.addInfoMessage("Valoración exitosa al producto" + p.getNombre());
        }
    }

    //METODO QUE ME LISTA LOS COMENTARIOS Y VALORACION DE CADA UNO DE UN PRODUCTO EN ESPECIFICO
    public void listarComentariosDeProducto(Producto p) {
        comentariosDeProducto = new LinkedList<>();
        for (Comentario c : comentarios) {
            if (c.getProducto().getReferencia().equals(p.getReferencia())) {
                comentariosDeProducto.add(c);
            }
        }
        calcularPromedioRatingProducto(p);
        contarEstrellitas();
    }

    //METODO QUE ME CONSULTA TODOS LOS COMENTARIOS DE LA DB
    public void consultarComentarios() {
        comentarios = comenserv.consultarTodo(Comentario.class);
    }

    //METODO QUE ME PERMITE CALCULAR EL PROMEDIO DE VALORACIÓN DE UN PRODUCTO EN ESPECIFICO EN BASE A SUS COMENTARIOS
    public void calcularPromedioRatingProducto(Producto p) {
        valoracionProducto = 0;
        int total = 0;
        for (Comentario c : comentariosDeProducto) {
            if (c.getProducto().getReferencia().equals(p.getReferencia())) {
                valoracionProducto += c.getRating();
                total += 1;
            }
        }
        if (valoracionProducto != 0) {

            double r = (valoracionProducto) * 10 / total;
            System.out.println(r);
            valoracionProductoDouble = (double) r / 10;
            valoracionProducto = (int) Math.round(valoracionProducto / total);
        } else {
            valoracionProducto = 0;
            valoracionProductoDouble = 0.0;
        }
    }

    //METODO QUE ME PERMITE CONTAR CUANTAS ESTRELLAS DE CADA PUNTAJE TIENE CADA PRODUCTO
    public void contarEstrellitas() {
        starOne = 0;
        starTwo = 0;
        starThree = 0;
        starFour = 0;
        starFive = 0;
        for (Comentario c : comentariosDeProducto) {
            if (c.getRating() == 5) {
                starFive += 1;
            } else if (c.getRating() == 4) {
                starFour += 1;
            } else if (c.getRating() == 3) {
                starThree += 1;
            } else if (c.getRating() == 2) {
                starTwo += 1;
            } else if (c.getRating() == 1) {
                starOne += 1;
            }
        }
    }

    public int getValoracionProducto() {
        return valoracionProducto;
    }

    public void setValoracionProducto(int valoracionProducto) {
        this.valoracionProducto = valoracionProducto;
    }

    public double getValoracionProductoDouble() {
        return valoracionProductoDouble;
    }

    public void setValoracionProductoDouble(double valoracionProductoDouble) {
        this.valoracionProductoDouble = valoracionProductoDouble;
    }

    /**
     * @return the comentario
     */
    public Comentario getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the comentarios
     */
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Comentario> getComentariosDeProducto() {
        return comentariosDeProducto;
    }

    public void setComentariosDeProducto(List<Comentario> comentariosDeProducto) {
        this.comentariosDeProducto = comentariosDeProducto;
    }

    public int getStarOne() {
        return starOne;
    }

    public void setStarOne(int starOne) {
        this.starOne = starOne;
    }

    public int getStarTwo() {
        return starTwo;
    }

    public void setStarTwo(int starTwo) {
        this.starTwo = starTwo;
    }

    public int getStarThree() {
        return starThree;
    }

    public void setStarThree(int starThree) {
        this.starThree = starThree;
    }

    public int getStarFour() {
        return starFour;
    }

    public void setStarFour(int starFour) {
        this.starFour = starFour;
    }

    public int getStarFive() {
        return starFive;
    }

    public void setStarFive(int starFive) {
        this.starFive = starFive;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Comentario;
import com.entity.Producto;
import com.implDao.IComentario;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jairo
 */
public class ComentarioService extends ImplDao<Comentario, Long> implements IComentario, Serializable {

    public List<Comentario> obtenerComentariosDeProducto(Producto p) {
        EntityManager em = ImplDao.getEntityManagger();
        List<Comentario> comentariosDeProducto = new LinkedList<>();
        em.getTransaction().begin();

        String q = "select c from Comentario c where c.producto.id = ?1";//Se cosulta a las cotizaciones en base a su estado
        Query qu = em.createQuery(q)
                .setParameter(1, p.getId());
        comentariosDeProducto = qu.getResultList();

        return comentariosDeProducto;//Retorna las cotizaciones solicitadas en lista
    }
}

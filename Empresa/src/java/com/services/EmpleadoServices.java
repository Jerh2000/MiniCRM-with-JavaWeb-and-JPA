/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.dao.ImplDao;
import com.entity.Empleado;
import com.entity.Usuario;
import com.implDao.IEmpleado;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jairo
 */
public class EmpleadoServices extends ImplDao<Empleado, Long> implements IEmpleado, Serializable {

    public boolean validarEmpleado(String log) {
        System.out.println(log);
        boolean existe = false;
        EntityManager em = ImplDao.getEntityManagger();
        Usuario usu = new Usuario();
        em.getTransaction().begin();
        try {
            String q = "select u from Usuario u where u.login = ?1";//Se cosulta a usuario en base a su login y password
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, log);
            usu = (Usuario) qu.getSingleResult();//Guarda un objeto de tipo usuario que capto de la consulta
            if (usu.getLogin().equals(log)) {
                existe = true;
            }
            System.out.println(usu.getLogin() + "ddddddfdsfhs");
        } catch (Exception ex) {

        } finally {
            em.close();
        }

        return existe;//Retorna el usuario consultado
    }
}

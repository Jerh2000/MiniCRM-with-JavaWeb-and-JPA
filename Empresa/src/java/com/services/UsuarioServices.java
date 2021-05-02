
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.utilidades.FacesUtil;
import com.dao.ImplDao;
import com.entity.Usuario;
import com.implDao.IUsuario;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Jairo
 */
public class UsuarioServices extends ImplDao<Usuario, Long> implements IUsuario, Serializable {

    public Usuario ingresar(String log, String pass) {
        EntityManager em = ImplDao.getEntityManagger();
        Usuario usu = new Usuario();
        em.getTransaction().begin();
        try {
            String q = "select u from Usuario u where u.login = ?1 and u.password = ?2";//Se cosulta a usuario en base a su login y password
            System.out.println(" Consulta: " + q);
            Query qu = em.createQuery(q)
                    .setParameter(1, log)
                    .setParameter(2, pass);
            usu = (Usuario) qu.getSingleResult();//Guarda un objeto de tipo usuario que capto de la consulta
        } catch (Exception ex) {
            FacesUtil.addErrorMessage("¡Nombre de usuario o contraseña incorrecta!", ex.getMessage());
        } finally {
            em.close();
        }

        return usu;//Retorna el usuario consultado
    }

}

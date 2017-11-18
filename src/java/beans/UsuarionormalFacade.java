/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entidades.Usuarionormal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Leyaim
 */
@Stateless
public class UsuarionormalFacade extends AbstractFacade<Usuarionormal> {

    @PersistenceContext(unitName = "ProyectoIntegradorPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarionormalFacade() {
        super(Usuarionormal.class);
    }
    
}

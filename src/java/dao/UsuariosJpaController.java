/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TicketAsignado;
import entidades.Usuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Leyaim
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws RollbackFailureException, Exception {
        if (usuarios.getTicketAsignadoCollection() == null) {
            usuarios.setTicketAsignadoCollection(new ArrayList<TicketAsignado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<TicketAsignado> attachedTicketAsignadoCollection = new ArrayList<TicketAsignado>();
            for (TicketAsignado ticketAsignadoCollectionTicketAsignadoToAttach : usuarios.getTicketAsignadoCollection()) {
                ticketAsignadoCollectionTicketAsignadoToAttach = em.getReference(ticketAsignadoCollectionTicketAsignadoToAttach.getClass(), ticketAsignadoCollectionTicketAsignadoToAttach.getIDTicketAsignado());
                attachedTicketAsignadoCollection.add(ticketAsignadoCollectionTicketAsignadoToAttach);
            }
            usuarios.setTicketAsignadoCollection(attachedTicketAsignadoCollection);
            em.persist(usuarios);
            for (TicketAsignado ticketAsignadoCollectionTicketAsignado : usuarios.getTicketAsignadoCollection()) {
                Usuarios oldIdUsuarioOfTicketAsignadoCollectionTicketAsignado = ticketAsignadoCollectionTicketAsignado.getIdUsuario();
                ticketAsignadoCollectionTicketAsignado.setIdUsuario(usuarios);
                ticketAsignadoCollectionTicketAsignado = em.merge(ticketAsignadoCollectionTicketAsignado);
                if (oldIdUsuarioOfTicketAsignadoCollectionTicketAsignado != null) {
                    oldIdUsuarioOfTicketAsignadoCollectionTicketAsignado.getTicketAsignadoCollection().remove(ticketAsignadoCollectionTicketAsignado);
                    oldIdUsuarioOfTicketAsignadoCollectionTicketAsignado = em.merge(oldIdUsuarioOfTicketAsignadoCollectionTicketAsignado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Collection<TicketAsignado> ticketAsignadoCollectionOld = persistentUsuarios.getTicketAsignadoCollection();
            Collection<TicketAsignado> ticketAsignadoCollectionNew = usuarios.getTicketAsignadoCollection();
            List<String> illegalOrphanMessages = null;
            for (TicketAsignado ticketAsignadoCollectionOldTicketAsignado : ticketAsignadoCollectionOld) {
                if (!ticketAsignadoCollectionNew.contains(ticketAsignadoCollectionOldTicketAsignado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TicketAsignado " + ticketAsignadoCollectionOldTicketAsignado + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TicketAsignado> attachedTicketAsignadoCollectionNew = new ArrayList<TicketAsignado>();
            for (TicketAsignado ticketAsignadoCollectionNewTicketAsignadoToAttach : ticketAsignadoCollectionNew) {
                ticketAsignadoCollectionNewTicketAsignadoToAttach = em.getReference(ticketAsignadoCollectionNewTicketAsignadoToAttach.getClass(), ticketAsignadoCollectionNewTicketAsignadoToAttach.getIDTicketAsignado());
                attachedTicketAsignadoCollectionNew.add(ticketAsignadoCollectionNewTicketAsignadoToAttach);
            }
            ticketAsignadoCollectionNew = attachedTicketAsignadoCollectionNew;
            usuarios.setTicketAsignadoCollection(ticketAsignadoCollectionNew);
            usuarios = em.merge(usuarios);
            for (TicketAsignado ticketAsignadoCollectionNewTicketAsignado : ticketAsignadoCollectionNew) {
                if (!ticketAsignadoCollectionOld.contains(ticketAsignadoCollectionNewTicketAsignado)) {
                    Usuarios oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado = ticketAsignadoCollectionNewTicketAsignado.getIdUsuario();
                    ticketAsignadoCollectionNewTicketAsignado.setIdUsuario(usuarios);
                    ticketAsignadoCollectionNewTicketAsignado = em.merge(ticketAsignadoCollectionNewTicketAsignado);
                    if (oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado != null && !oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado.equals(usuarios)) {
                        oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado.getTicketAsignadoCollection().remove(ticketAsignadoCollectionNewTicketAsignado);
                        oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado = em.merge(oldIdUsuarioOfTicketAsignadoCollectionNewTicketAsignado);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TicketAsignado> ticketAsignadoCollectionOrphanCheck = usuarios.getTicketAsignadoCollection();
            for (TicketAsignado ticketAsignadoCollectionOrphanCheckTicketAsignado : ticketAsignadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the TicketAsignado " + ticketAsignadoCollectionOrphanCheckTicketAsignado + " in its ticketAsignadoCollection field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

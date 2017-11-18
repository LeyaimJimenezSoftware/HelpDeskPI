/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Ticket;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TicketAsignado;
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
public class TicketJpaController implements Serializable {

    public TicketJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ticket ticket) throws RollbackFailureException, Exception {
        if (ticket.getTicketAsignadoCollection() == null) {
            ticket.setTicketAsignadoCollection(new ArrayList<TicketAsignado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<TicketAsignado> attachedTicketAsignadoCollection = new ArrayList<TicketAsignado>();
            for (TicketAsignado ticketAsignadoCollectionTicketAsignadoToAttach : ticket.getTicketAsignadoCollection()) {
                ticketAsignadoCollectionTicketAsignadoToAttach = em.getReference(ticketAsignadoCollectionTicketAsignadoToAttach.getClass(), ticketAsignadoCollectionTicketAsignadoToAttach.getIDTicketAsignado());
                attachedTicketAsignadoCollection.add(ticketAsignadoCollectionTicketAsignadoToAttach);
            }
            ticket.setTicketAsignadoCollection(attachedTicketAsignadoCollection);
            em.persist(ticket);
            for (TicketAsignado ticketAsignadoCollectionTicketAsignado : ticket.getTicketAsignadoCollection()) {
                Ticket oldIdTicketOfTicketAsignadoCollectionTicketAsignado = ticketAsignadoCollectionTicketAsignado.getIdTicket();
                ticketAsignadoCollectionTicketAsignado.setIdTicket(ticket);
                ticketAsignadoCollectionTicketAsignado = em.merge(ticketAsignadoCollectionTicketAsignado);
                if (oldIdTicketOfTicketAsignadoCollectionTicketAsignado != null) {
                    oldIdTicketOfTicketAsignadoCollectionTicketAsignado.getTicketAsignadoCollection().remove(ticketAsignadoCollectionTicketAsignado);
                    oldIdTicketOfTicketAsignadoCollectionTicketAsignado = em.merge(oldIdTicketOfTicketAsignadoCollectionTicketAsignado);
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

    public void edit(Ticket ticket) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ticket persistentTicket = em.find(Ticket.class, ticket.getIdTicket());
            Collection<TicketAsignado> ticketAsignadoCollectionOld = persistentTicket.getTicketAsignadoCollection();
            Collection<TicketAsignado> ticketAsignadoCollectionNew = ticket.getTicketAsignadoCollection();
            List<String> illegalOrphanMessages = null;
            for (TicketAsignado ticketAsignadoCollectionOldTicketAsignado : ticketAsignadoCollectionOld) {
                if (!ticketAsignadoCollectionNew.contains(ticketAsignadoCollectionOldTicketAsignado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TicketAsignado " + ticketAsignadoCollectionOldTicketAsignado + " since its idTicket field is not nullable.");
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
            ticket.setTicketAsignadoCollection(ticketAsignadoCollectionNew);
            ticket = em.merge(ticket);
            for (TicketAsignado ticketAsignadoCollectionNewTicketAsignado : ticketAsignadoCollectionNew) {
                if (!ticketAsignadoCollectionOld.contains(ticketAsignadoCollectionNewTicketAsignado)) {
                    Ticket oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado = ticketAsignadoCollectionNewTicketAsignado.getIdTicket();
                    ticketAsignadoCollectionNewTicketAsignado.setIdTicket(ticket);
                    ticketAsignadoCollectionNewTicketAsignado = em.merge(ticketAsignadoCollectionNewTicketAsignado);
                    if (oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado != null && !oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado.equals(ticket)) {
                        oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado.getTicketAsignadoCollection().remove(ticketAsignadoCollectionNewTicketAsignado);
                        oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado = em.merge(oldIdTicketOfTicketAsignadoCollectionNewTicketAsignado);
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
                Integer id = ticket.getIdTicket();
                if (findTicket(id) == null) {
                    throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.");
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
            Ticket ticket;
            try {
                ticket = em.getReference(Ticket.class, id);
                ticket.getIdTicket();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TicketAsignado> ticketAsignadoCollectionOrphanCheck = ticket.getTicketAsignadoCollection();
            for (TicketAsignado ticketAsignadoCollectionOrphanCheckTicketAsignado : ticketAsignadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ticket (" + ticket + ") cannot be destroyed since the TicketAsignado " + ticketAsignadoCollectionOrphanCheckTicketAsignado + " in its ticketAsignadoCollection field has a non-nullable idTicket field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ticket);
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

    public List<Ticket> findTicketEntities() {
        return findTicketEntities(true, -1, -1);
    }

    public List<Ticket> findTicketEntities(int maxResults, int firstResult) {
        return findTicketEntities(false, maxResults, firstResult);
    }

    private List<Ticket> findTicketEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ticket.class));
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

    public Ticket findTicket(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ticket.class, id);
        } finally {
            em.close();
        }
    }

    public int getTicketCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ticket> rt = cq.from(Ticket.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

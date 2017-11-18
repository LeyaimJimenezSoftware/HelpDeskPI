/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuarios;
import entidades.Ticket;
import entidades.TicketAsignado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.transaction.UserTransaction;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Leyaim
 */
public class TicketAsignadoJpaController implements Serializable {

    public TicketAsignadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    public void create(TicketAsignado ticketAsignado) throws RollbackFailureException, Exception {
        
    
        
       //ticketAsignado.getIdTicket()
        
        
    
//        if( findTicketIDTicket(ticketAsignado) != true ){
                EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios idUsuario = ticketAsignado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                ticketAsignado.setIdUsuario(idUsuario);
            }
            Ticket idTicket = ticketAsignado.getIdTicket();
            if (idTicket != null) {
                idTicket = em.getReference(idTicket.getClass(), idTicket.getIdTicket());
                ticketAsignado.setIdTicket(idTicket);
            }
            em.persist(ticketAsignado);
            if (idUsuario != null) {
                idUsuario.getTicketAsignadoCollection().add(ticketAsignado);
                idUsuario = em.merge(idUsuario);
            }
            if (idTicket != null) {
                idTicket.getTicketAsignadoCollection().add(ticketAsignado);
                idTicket = em.merge(idTicket);
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
//        }else{
//            throw new RollbackFailureException("An error occurred attempting to roll back the transaction.");
//           
//        }
    }

    public void edit(TicketAsignado ticketAsignado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TicketAsignado persistentTicketAsignado = em.find(TicketAsignado.class, ticketAsignado.getIDTicketAsignado());
            Usuarios idUsuarioOld = persistentTicketAsignado.getIdUsuario();
            Usuarios idUsuarioNew = ticketAsignado.getIdUsuario();
            Ticket idTicketOld = persistentTicketAsignado.getIdTicket();
            Ticket idTicketNew = ticketAsignado.getIdTicket();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                ticketAsignado.setIdUsuario(idUsuarioNew);
            }
            if (idTicketNew != null) {
                idTicketNew = em.getReference(idTicketNew.getClass(), idTicketNew.getIdTicket());
                ticketAsignado.setIdTicket(idTicketNew);
            }
            ticketAsignado = em.merge(ticketAsignado);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getTicketAsignadoCollection().remove(ticketAsignado);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getTicketAsignadoCollection().add(ticketAsignado);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idTicketOld != null && !idTicketOld.equals(idTicketNew)) {
                idTicketOld.getTicketAsignadoCollection().remove(ticketAsignado);
                idTicketOld = em.merge(idTicketOld);
            }
            if (idTicketNew != null && !idTicketNew.equals(idTicketOld)) {
                idTicketNew.getTicketAsignadoCollection().add(ticketAsignado);
                idTicketNew = em.merge(idTicketNew);
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
                Integer id = ticketAsignado.getIDTicketAsignado();
                if (findTicketAsignado(id) == null) {
                    throw new NonexistentEntityException("The ticketAsignado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TicketAsignado ticketAsignado;
            try {
                ticketAsignado = em.getReference(TicketAsignado.class, id);
                ticketAsignado.getIDTicketAsignado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ticketAsignado with id " + id + " no longer exists.", enfe);
            }
            Usuarios idUsuario = ticketAsignado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getTicketAsignadoCollection().remove(ticketAsignado);
                idUsuario = em.merge(idUsuario);
            }
            Ticket idTicket = ticketAsignado.getIdTicket();
            if (idTicket != null) {
                idTicket.getTicketAsignadoCollection().remove(ticketAsignado);
                idTicket = em.merge(idTicket);
            }
            em.remove(ticketAsignado);
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

    public List<TicketAsignado> findTicketAsignadoEntities() {
        return findTicketAsignadoEntities(true, -1, -1);
    }

    public List<TicketAsignado> findTicketAsignadoEntities(int maxResults, int firstResult) {
        return findTicketAsignadoEntities(false, maxResults, firstResult);
    }

    private List<TicketAsignado> findTicketAsignadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TicketAsignado.class));
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

    public TicketAsignado findTicketAsignado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TicketAsignado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTicketAsignadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TicketAsignado> rt = cq.from(TicketAsignado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      public boolean findTicketIDTicket(TicketAsignado ticketAsignado) {
        EntityManager em = getEntityManager();
        PreparedStatement pst = null;
        ResultSet rs = null;
         int t;
         conect.conexion bd = new conect.conexion();
         
         t = Integer.parseInt(ticketAsignado.getIdTicket().toString());
         
    
    try  {
    
        String consulta = "SELECT * FROM `ticket_asignado` WHERE Id_Ticket = ?";
        pst =  bd.getConexion().prepareStatement(consulta);
        pst.setInt(1, t);
       
        rs = pst.executeQuery();
        
        if(rs.absolute(1)){
        
           return true;
        }
        
        
    }catch(Exception e){
        System.out.println("error"+e);
        
    }finally{
        try{
    if(bd.getConexion() != null){
        bd.getConexion().close();
    }
    if(pst != null){
       pst.close();
    }
    if(rs !=null){
       rs.close();
    }
    
        }catch(Exception e){
              System.out.println("error"+e);
        }
        
    }
    
    
    
    return false;
    }
         
         
         
       
 
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Leyaim
 */
@Entity
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
    @NamedQuery(name = "Ticket.findByIdTicket", query = "SELECT t FROM Ticket t WHERE t.idTicket = :idTicket"),
    @NamedQuery(name = "Ticket.findByComentarios", query = "SELECT t FROM Ticket t WHERE t.comentarios = :comentarios"),
    @NamedQuery(name = "Ticket.findByTipodeTicket", query = "SELECT t FROM Ticket t WHERE t.tipodeTicket = :tipodeTicket"),
    @NamedQuery(name = "Ticket.findByFechadeCreacion", query = "SELECT t FROM Ticket t WHERE t.fechadeCreacion = :fechadeCreacion"),
    @NamedQuery(name = "Ticket.findByCorreo", query = "SELECT t FROM Ticket t WHERE t.correo = :correo")})
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Ticket")
    private Integer idTicket;
    @Size(max = 100)
    @Column(name = "Comentarios")
    private String comentarios;
    @Size(max = 20)
    @Column(name = "Tipo_de_Ticket")
    private String tipodeTicket;
    @Column(name = "Fecha_de_Creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadeCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Correo")
    private String correo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTicket")
    private Collection<TicketAsignado> ticketAsignadoCollection;

    public Ticket() {
    }

    public Ticket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public Ticket(Integer idTicket, String correo) {
        this.idTicket = idTicket;
        this.correo = correo;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getTipodeTicket() {
        return tipodeTicket;
    }

    public void setTipodeTicket(String tipodeTicket) {
        this.tipodeTicket = tipodeTicket;
    }

    public Date getFechadeCreacion() {
        return fechadeCreacion;
    }

    public void setFechadeCreacion(Date fechadeCreacion) {
        this.fechadeCreacion = fechadeCreacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @XmlTransient
    public Collection<TicketAsignado> getTicketAsignadoCollection() {
        return ticketAsignadoCollection;
    }

    public void setTicketAsignadoCollection(Collection<TicketAsignado> ticketAsignadoCollection) {
        this.ticketAsignadoCollection = ticketAsignadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTicket != null ? idTicket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.idTicket == null && other.idTicket != null) || (this.idTicket != null && !this.idTicket.equals(other.idTicket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
       
        
        return "" + idTicket + "";
    }
    
}

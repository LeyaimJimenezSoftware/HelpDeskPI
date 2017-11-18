/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leyaim
 */
@Entity
@Table(name = "ticket_asignado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketAsignado.findAll", query = "SELECT t FROM TicketAsignado t"),
    @NamedQuery(name = "TicketAsignado.findByIDTicketAsignado", query = "SELECT t FROM TicketAsignado t WHERE t.iDTicketAsignado = :iDTicketAsignado"),
    @NamedQuery(name = "TicketAsignado.findByFechadeInicio", query = "SELECT t FROM TicketAsignado t WHERE t.fechadeInicio = :fechadeInicio"),
    @NamedQuery(name = "TicketAsignado.findByFechadeFin", query = "SELECT t FROM TicketAsignado t WHERE t.fechadeFin = :fechadeFin"),
    @NamedQuery(name = "TicketAsignado.findByEstadodeTicket", query = "SELECT t FROM TicketAsignado t WHERE t.estadodeTicket = :estadodeTicket")})
public class TicketAsignado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TicketAsignado")
    private Integer iDTicketAsignado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_de_Inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadeInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_de_Fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadeFin;
    @Size(max = 15)
    @Column(name = "Estado_de_Ticket")
    private String estadodeTicket;
    @JoinColumn(name = "Id_Usuario", referencedColumnName = "Id_Usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @JoinColumn(name = "Id_Ticket", referencedColumnName = "Id_Ticket")
    @ManyToOne(optional = false)
    private Ticket idTicket;

    public TicketAsignado() {
    }

    public TicketAsignado(Integer iDTicketAsignado) {
        this.iDTicketAsignado = iDTicketAsignado;
    }

    public TicketAsignado(Integer iDTicketAsignado, Date fechadeInicio, Date fechadeFin) {
        this.iDTicketAsignado = iDTicketAsignado;
        this.fechadeInicio = fechadeInicio;
        this.fechadeFin = fechadeFin;
    }

    public Integer getIDTicketAsignado() {
        return iDTicketAsignado;
    }

    public void setIDTicketAsignado(Integer iDTicketAsignado) {
        this.iDTicketAsignado = iDTicketAsignado;
    }

    public Date getFechadeInicio() {
        return fechadeInicio;
    }

    public void setFechadeInicio(Date fechadeInicio) {
        this.fechadeInicio = fechadeInicio;
    }

    public Date getFechadeFin() {
        return fechadeFin;
    }

    public void setFechadeFin(Date fechadeFin) {
        this.fechadeFin = fechadeFin;
    }

    public String getEstadodeTicket() {
        return estadodeTicket;
    }

    public void setEstadodeTicket(String estadodeTicket) {
        this.estadodeTicket = estadodeTicket;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Ticket getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Ticket idTicket) {
        this.idTicket = idTicket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDTicketAsignado != null ? iDTicketAsignado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketAsignado)) {
            return false;
        }
        TicketAsignado other = (TicketAsignado) object;
        if ((this.iDTicketAsignado == null && other.iDTicketAsignado != null) || (this.iDTicketAsignado != null && !this.iDTicketAsignado.equals(other.iDTicketAsignado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TicketAsignado[ iDTicketAsignado=" + iDTicketAsignado + " ]";
    }
    
}

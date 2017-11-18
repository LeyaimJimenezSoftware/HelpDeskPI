/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leyaim
 */
@Entity
@Table(name = "usuarionormal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarionormal.findAll", query = "SELECT u FROM Usuarionormal u"),
    @NamedQuery(name = "Usuarionormal.findByIdUsuarioN", query = "SELECT u FROM Usuarionormal u WHERE u.idUsuarioN = :idUsuarioN"),
    @NamedQuery(name = "Usuarionormal.findByNombre", query = "SELECT u FROM Usuarionormal u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuarionormal.findByDependencia", query = "SELECT u FROM Usuarionormal u WHERE u.dependencia = :dependencia"),
    @NamedQuery(name = "Usuarionormal.findByCorreo", query = "SELECT u FROM Usuarionormal u WHERE u.correo = :correo"),
    @NamedQuery(name = "Usuarionormal.findByContrase\u00f1a", query = "SELECT u FROM Usuarionormal u WHERE u.contrase\u00f1a = :contrase\u00f1a")})
public class Usuarionormal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_UsuarioN")
    private Integer idUsuarioN;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Dependencia")
    private String dependencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Contrase\u00f1a")
    private String contraseña;

    public Usuarionormal() {
    }

    public Usuarionormal(Integer idUsuarioN) {
        this.idUsuarioN = idUsuarioN;
    }

    public Usuarionormal(Integer idUsuarioN, String nombre, String dependencia, String correo, String contraseña) {
        this.idUsuarioN = idUsuarioN;
        this.nombre = nombre;
        this.dependencia = dependencia;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public Integer getIdUsuarioN() {
        return idUsuarioN;
    }

    public void setIdUsuarioN(Integer idUsuarioN) {
        this.idUsuarioN = idUsuarioN;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarioN != null ? idUsuarioN.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarionormal)) {
            return false;
        }
        Usuarionormal other = (Usuarionormal) object;
        if ((this.idUsuarioN == null && other.idUsuarioN != null) || (this.idUsuarioN != null && !this.idUsuarioN.equals(other.idUsuarioN))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuarionormal[ idUsuarioN=" + idUsuarioN + " ]";
    }
    
}

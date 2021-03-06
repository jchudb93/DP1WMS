package com.dp1wms.model;

public class Cliente {

    private long idCliente;
    private String numDoc;
    private String razonSocial;
    private String telefono;
    private String direccion;
    private String email;
    private boolean activo;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void copyFrom(Cliente c){
        this.idCliente = c.idCliente;
        this.direccion = c.direccion;
        this.email = c.email;
        this.numDoc = c.numDoc;
        this.razonSocial = c.razonSocial;
        this.telefono = c.telefono;
        this.activo = c.activo;
    }

    public Cliente(){

    }

    public Cliente(Cliente c){
        this.idCliente = c.idCliente;
        this.direccion = c.direccion;
        this.email = c.email;
        this.numDoc = c.numDoc;
        this.razonSocial = c.razonSocial;
        this.telefono = c.telefono;
        this.activo = c.activo;
    }

}

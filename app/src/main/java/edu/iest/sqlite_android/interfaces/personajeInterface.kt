package edu.iest.sqlite_android.interfaces

import edu.iest.sqlite_android.modelos.Personaje

public interface personajeInterface {
    fun personajeEliminado()
    fun editarPersonaje(personaje: Personaje)

}
import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  private readonly config: Config;
  private readonly routes: RouteInfo[] = [];
  private usuario: Usuario;

  constructor() {
    this.config = {
      title: 'App',
      url: 'http://localhost:8080'
    };
  }

  ngOnInit(): void {
  }

  public getConfig(): Config {
    return this.config;
  }

  public setUsuario(usuario: Usuario) {
    this.usuario = usuario;
    localStorage.setItem('user', JSON.stringify(usuario));
    localStorage.setItem('routes', JSON.stringify(this.asignarFunciones()));
  }

  public getUsuario(): Usuario {
    return JSON.parse(localStorage.getItem('user'));
  }

  public getRoutes(): RouteInfo[] {
    return JSON.parse(localStorage.getItem('routes'));
  }

  private asignarFunciones(): RouteInfo[] {
    if (this.buscar('administrador')) {
      this.routes.push(
        {path: '/usuarios', title: 'Usuarios', icon: 'ni ni-single-02 text-yellow', class: ''},
        {path: '/formatos', title: 'Formatos', icon: 'ni-bullet-list-67 text-red', class: ''},
        {path: '/par-valor', title: 'Parámetro Valor', icon: 'ni ni-book-bookmark text-info', class: ''}
      )
    }

    if (this.buscar('usuario operativo')) {
      this.routes.push(
        {path: '/carga-archivo', title: 'Cargar Archivo', icon: 'ni ni-archive-2 text-blue', class: ''}
      )
    }

    return this.routes;
  }

  private buscar(nombre: string): boolean {
    let respuesta = false;
    this.usuario.permisos.map((permisos) => {
      if (permisos.nombre === nombre) {
        respuesta = true;
      }
    });
    return respuesta;
  }
}

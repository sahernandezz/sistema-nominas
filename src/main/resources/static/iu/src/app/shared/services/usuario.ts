import getToken from './token-config';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../../app.component';

@Injectable({
  providedIn: 'root'
})

export class UsuarioHttp {
  app: AppComponent;
  url: string;

  constructor(private http: HttpClient) {
    this.app = new AppComponent();
    this.url = '/usuario/api/v1';
  }

  public listaUsuarios(): any {
    return this.http.get<Usuario[]>(this.app.getConfig().url + this.url + '/lista', {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public listaPermisos(): any {
    return this.http.get<Rol[]>(this.app.getConfig().url + '/permisos/api/v1/lista', {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public registrarUsuario(usuario: Usuario): any {
    return this.http.post(this.app.getConfig().url + this.url + `/crear`, usuario, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public actualizarUsuario(usuario: Usuario): any {
    return this.http.put(this.app.getConfig().url + this.url + `/actualizar`, usuario, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public estadoUsuario(id: number): any {
    return this.http.put(this.app.getConfig().url + this.url + `/estado`, id, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }
}

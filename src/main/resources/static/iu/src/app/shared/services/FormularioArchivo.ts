import getToken from './token-config';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../../app.component';

@Injectable({
  providedIn: 'root'
})

export class FormularioArchivoHttp {
  app: AppComponent;
  url: string;

  constructor(private http: HttpClient) {
    this.app = new AppComponent();
    this.url = '/formato-archivo/api/v1';
  }

  public listaFormularios(): any {
    return this.http.get<FormatoArchivo[]>(this.app.getConfig().url + this.url + '/lista', {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public listaFormulariosActivos(): any {
    return this.http.get<FormatoArchivo[]>(this.app.getConfig().url + this.url + '/lista/activos', {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public registrarFormulario(formatoArchivo: FormatoArchivo): any {
    return this.http.post(this.app.getConfig().url + this.url + `/crear`, formatoArchivo, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public actualizarFormulario(formatoArchivo: FormatoArchivo): any {
    return this.http.put(this.app.getConfig().url + this.url + `/actualizar`, formatoArchivo, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public estadoFormulario(id: number): any {
    return this.http.put(this.app.getConfig().url + this.url + `/estado`, id, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }
}

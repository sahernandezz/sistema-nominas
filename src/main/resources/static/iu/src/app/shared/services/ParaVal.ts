import getToken from './token-config';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../../app.component';

@Injectable({
  providedIn: 'root'
})

export class ParaValHttp {
  app: AppComponent;
  url: string;

  constructor(private http: HttpClient) {
    this.app = new AppComponent();
    this.url = '/parametro-valor/api/v1';
  }

  public listaParVal(): any {
    return this.http.get<ParaVal[]>(this.app.getConfig().url + this.url + '/lista', {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public registrarParVal(paraVal: ParaVal): any {
    return this.http.post(this.app.getConfig().url + this.url + `/crear`, paraVal, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public actualizarParVal(paraVal: ParaVal): any {
    return this.http.put(this.app.getConfig().url + this.url + `/actualizar`, paraVal, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }

  public estadoParVal(id: number): any {
    return this.http.put(this.app.getConfig().url + this.url + `/estado`, id, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }
}

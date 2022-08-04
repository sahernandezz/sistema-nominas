import getToken from './token-config';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../../app.component';

@Injectable({
  providedIn: 'root'
})

export class CargaArchivoHttp {
  app: AppComponent;
  url: string;

  constructor(private http: HttpClient) {
    this.app = new AppComponent();
    this.url = '/carga-archivo/api/v1';
  }

  public guardarValidacionArchivo(file: any): any {
    let formData = new FormData();
    formData.append('file', file);
    return this.http.post(this.app.getConfig().url + this.url + `/validar`, formData, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }
}

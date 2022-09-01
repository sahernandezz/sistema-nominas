import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {AppComponent} from '../../app.component';
import getToken from "./token-config";

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

  public guardarValidacionArchivo(file: any, formato: FormatoArchivo): any {
    let formData = new FormData();
    formData.append('file', file);
    return this.http.post(this.app.getConfig().url
      + this.url + `/validar/${formato.id}`, formData, {
      headers: {
        'Authorization': `Bearer ${getToken()}`
      }
    });
  }
}

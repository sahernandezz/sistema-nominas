import getToken from './token-config';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppComponent} from '../../app.component';

@Injectable({
  providedIn: 'root'
})

export class LoginHttp {
  app: AppComponent;

  constructor(private http: HttpClient) {
    this.app = new AppComponent();
  }

  public login(auth: Auth): any {
    return this.http.post(this.app.getConfig().url + `/auth/api/v1/login`, auth);
  }
}

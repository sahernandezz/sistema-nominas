import {Component, OnInit, OnDestroy} from '@angular/core';
import {AppComponent} from '../../app.component';
import {Injectable} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {LoginHttp} from '../../shared/services/login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

@Injectable({
  providedIn: 'root'
})
export class LoginComponent implements OnInit, OnDestroy {
  app: AppComponent;
  buttonSummit: Boolean;

  constructor(private route: ActivatedRoute,
              private router: Router, private httpLogin: LoginHttp) {
    this.app = new AppComponent();
    this.buttonSummit = false;

    if (localStorage.getItem('USER_KEY') != null && localStorage.getItem('user') != null) {
      this.router.navigate(['/usuarios']).then(e => null);
    }
  }

  ngOnInit() {
  }

  ngOnDestroy() {
  }

  private login(auth: Auth, usuario: HTMLInputElement,
                group_usuario: HTMLInputElement, group_clave: HTMLInputElement,
                clave: HTMLInputElement): void {
    this.buttonSummit = true;
    this.httpLogin.login(auth).subscribe((data) => {
        localStorage.setItem('USER_KEY', data.object.token);
        group_usuario.classList.add('has-success');
        group_clave.classList.add('has-success');
        usuario.disabled = true;
        clave.disabled = true;
        this.app.setUsuario(data.object['usuario']);
        setTimeout(() => {
          this.router.navigate(['/usuarios']).then(e => null);
        }, 1000);
    }, (error) => {
      if (error.status === 400) {
        this.buttonSummit = false;
        group_usuario.classList.add('has-danger');
        group_clave.classList.add('has-danger');
      }
    });
  }

  datosUsuario(): void {
    try {
      const usuario = document.getElementById('input_login_usuario') as HTMLInputElement || null;
      const group_usuario = document.getElementById('form-group_login_usuario') as HTMLInputElement || null;
      const clave = document.getElementById('input_clave_usuario') as HTMLInputElement || null;
      const group_clave = document.getElementById('form-group_clave_usuario') as HTMLInputElement || null;
      (usuario.value == null || usuario.value === '') ? group_usuario.classList.add('has-danger') : group_usuario.classList.remove('has-danger');
      (clave.value == null || clave.value === '') ? group_clave.classList.add('has-danger') : group_clave.classList.remove('has-danger');

      this.login({
        login: usuario.value,
        clave: clave.value
      }, usuario, group_usuario, group_clave, clave);
    } catch (e) {
      this.buttonSummit = false;
    }
  }
}

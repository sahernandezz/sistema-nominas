import {Component, OnInit, OnDestroy} from '@angular/core';
import {AppComponent} from '../../app.component';
import {Injectable} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {LoginHttp} from '../../shared/services/login';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
  mensaje: String = '';

  constructor(private route: ActivatedRoute, public modalService: NgbModal,
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

  open(content: any) {
    this.modalService.open(content, {centered: true, ariaLabelledBy: 'modal-basic-title', backdrop: 'static'});
  }

  private login(auth: Auth, usuario: HTMLInputElement,
                group_usuario: HTMLInputElement, group_clave: HTMLInputElement,
                clave: HTMLInputElement, modalMensaje: any): void {
    this.buttonSummit = true;
    this.httpLogin.login(auth).subscribe((data) => {
      localStorage.setItem('USER_KEY', data.object.token);

      group_usuario.classList.add('has-success');
      group_clave.classList.add('has-success');

      usuario.disabled = true;
      clave.disabled = true;
      this.app.setUsuario(data.object['usuario']);

      setTimeout(() => {
        this.router.navigate([this.app.getRoutes()[0].path]).then(e => null);
      }, 1000);
    }, (error) => {
      this.buttonSummit = false;
      group_usuario.classList.add('has-danger');
      group_clave.classList.add('has-danger');

      if (error.status === 0 || error.status === 500) {
        this.mensajeModal(modalMensaje, "Hay inconvenientes, intente nuevamente más tarde");
      }
    });
  }

  datosUsuario(modalMensaje: any): void {
    try {
      const usuario = document.getElementById('input_login_usuario') as HTMLInputElement || null;
      const group_usuario = document.getElementById('form-group_login_usuario') as HTMLInputElement || null;
      const clave = document.getElementById('input_clave_usuario') as HTMLInputElement || null;
      const group_clave = document.getElementById('form-group_clave_usuario') as HTMLInputElement || null;
      (usuario.value == null || usuario.value === '') ? group_usuario.classList.add('has-danger') : group_usuario.classList.remove('has-danger');
      (clave.value == null || clave.value === '') ? group_clave.classList.add('has-danger') : group_clave.classList.remove('has-danger');

      if (usuario.value !== null && usuario.value !== ''
        && clave.value !== null && clave.value !== '') {
        this.login({
          login: usuario.value,
          clave: clave.value
        }, usuario, group_usuario, group_clave, clave, modalMensaje);
      }
    } catch (e) {
      this.buttonSummit = false;
    }
  }

  mensajeModal(content: any, mensaje: string): void {
    this.open(content);
    this.mensaje = mensaje;
  }
}

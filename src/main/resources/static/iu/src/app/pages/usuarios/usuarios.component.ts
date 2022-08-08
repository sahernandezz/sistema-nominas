import {Component, OnInit, ViewChild} from '@angular/core';
import {AppComponent} from '../../app.component';
import {UsuarioHttp} from '../../shared/services/usuario';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {Router} from '@angular/router';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent implements OnInit {
  mensaje: String = '';
  eventoUsuario: Object = {estado: 'R', usuario: null};
  dataPermisos: Rol[];
  toppings = new FormControl('');
  displayedColumns: string[] = ['Login', 'Nombre', 'Acciones'];
  dataSource = new MatTableDataSource<Usuario>([]);
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(public modalService: NgbModal, private httpUsuario: UsuarioHttp,
              public app: AppComponent, private router: Router) {
    this.dataPermisos = this.getPermisos();
  }

  ngOnInit(): void {
    this.httpUsuario.listaUsuarios().subscribe((data) => {
      this.dataSource.data = data;
    }, (error) => {
      this.error401(error.status, null);
    });
  }

  refresh(): void {
    location.reload();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  open(content: any) {
    this.modalService.open(content, {centered: true, ariaLabelledBy: 'modal-basic-title', backdrop: 'static'});
  }

  openRegister(content: any): void {
    this.open(content);
    this.eventoUsuario = {estado: 'R'};
  }

  openUpdate(content: any, usuario: Usuario): void {
    this.open(content);
    this.eventoUsuario = {estado: 'U', usuario: usuario};
  }

  cambiarEstado(id: number, content: any, mensaje: any, content_resultado: any): void {
    this.open(content);
    this.httpUsuario.estadoUsuario(id).subscribe((data) => {
      setTimeout(() => {
        this.modalService.dismissAll('Cross click');
        this.ngOnInit();
        this.mensaje = data.message;
        this.open(content_resultado);
        setTimeout(() => {
          this.modalService.dismissAll('Cross click');
        }, 1200);
      }, 500);
    }, (error) => {
      this.error401(error, mensaje);
      this.modalService.dismissAll('Cross click');
      this.mensajeModal(mensaje, error.error.message);
    });
  }

  getPermisos(): Rol[] {
    const lista = [];
    this.httpUsuario.listaPermisos().subscribe(data => {
      lista.push(...data);
    }, (error) => {
      if (error.status === 401) {
        localStorage.clear();
        this.router.navigate(['/login']).then(e => null);
      }
    });

    return lista;
  }

  errorInput(inputId: String, mensaje: string): void {
    const from_group = document.getElementById('from_group_' + inputId) as HTMLInputElement || null;
    const text = document.getElementById('text_' + inputId) as HTMLInputElement || null;
    from_group.classList.add('has-danger');
    text.innerText = mensaje;
  }

  limpiarInput(inputId: String): void {
    const from_group = document.getElementById('from_group_' + inputId) as HTMLInputElement || null;
    const text = document.getElementById('text_' + inputId) as HTMLInputElement || null;
    from_group.classList.remove('has-danger');
    text.innerText = '';
  }

  selectInput(): Object {
    const ObjectResult = {};
    let i = 0;
    const listaInput = ['nombre_usuario', 'login_usuario'];
    for (const label of listaInput) {
      const input = document.getElementById(label) as HTMLInputElement || null;
      const from_group = document.getElementById('from_group_' + label) as HTMLInputElement || null;
      const text = document.getElementById('text_' + label) as HTMLInputElement || null;
      if (input.value === null || input.value === '') {
        from_group.classList.add('has-danger');
        text.innerText = 'Campo vació';
      } else {
        i++;
        from_group.classList.remove('has-danger');
        ObjectResult[label] = input.value;
        text.innerText = '';
      }
    }
    return i === listaInput.length ? ObjectResult : null;
  }

  validarClave(): string {
    let estado = null;
    const inputClave = document.getElementById('clave_usuario') as HTMLInputElement || null;
    const inputConfirmar = document.getElementById('confirmar_clave_usuario') as HTMLInputElement || null;
    const clave = inputClave;
    if (this.eventoUsuario['estado'] === 'R') {
      if (clave.value === null || clave.value === '') {
        this.errorInput('clave_usuario', 'Campo vació');
      }

      if (inputConfirmar.value === null || inputConfirmar.value === '') {
        this.errorInput('confirmar_clave_usuario', 'Campo vació');
      }
    }

    if (clave.value !== inputConfirmar.value) {
      this.errorInput('confirmar_clave_usuario', 'La contraseña no coincide');
    } else {
      estado = clave.value;
    }
    return estado;
  }

  parse(rol: Rol): string {
    return JSON.stringify(rol);
  }

  permisos(): Rol[] {
    const value: Rol[] = [];
    const select = document.getElementById('permisos') as HTMLInputElement || null;
    const from_group_select = document.getElementById('from_group_' + 'permisos') as HTMLInputElement || null;
    const text_select = document.getElementById('text_' + 'permisos') as HTMLInputElement || null;

    if (select.value === 'null') {
      from_group_select.classList.add('has-danger');
      text_select.innerText = 'Selección no valida';
    } else {
      from_group_select.classList.remove('has-danger');
      value.push(JSON.parse(select.value));
      text_select.innerText = '';
    }
    return value.length !== 0 ? value : null;
  }

  registrarUsuario = (e: Event, content: any, mensaje: any) => {
    e.preventDefault();
    let value = true;
    const valueInput = this.selectInput();
    const claveInput = this.validarClave();
    const permisosValor = this.permisos();
    value = valueInput === null ? false : value;
    value = claveInput === null ? false : value;
    value = permisosValor === null ? false : value;
    if (value) {
      this.httpUsuario.registrarUsuario({
        id: undefined,
        nombre: valueInput['nombre_usuario'],
        login: valueInput['login_usuario'],
        clave: claveInput,
        permisos: permisosValor,
        estado: undefined
      }).subscribe((data) => {
        this.completado(content);
      }, (error) => {
        this.error401(error, mensaje);
        this.mensajeModal(mensaje, error.error.message);
      });
    }
  }

  actualizarUsuario(e: Event, content: any, mensaje: any): void {
    e.preventDefault();
    const valueInput = this.selectInput();
    const claveInput = this.validarClave();
    const permisosValor = this.permisos();
    let value = true;
    value = claveInput === null ? false : value;
    value = valueInput === null ? false : value;
    value = permisosValor === null ? false : value;
    if (value) {
      this.httpUsuario.actualizarUsuario({
          id: this.eventoUsuario['usuario']['id'],
          nombre: valueInput['nombre_usuario'],
          login: valueInput['login_usuario'],
          clave: claveInput === '' ? this.eventoUsuario['usuario']['clave'] : claveInput,
          permisos: permisosValor,
          estado: this.eventoUsuario['usuario']['estado']
        }
      ).subscribe((data) => {
        this.completado(content);
      }, (error) => {
        this.error401(error, mensaje);
        this.mensajeModal(mensaje, error.error.message);
      });
    }
  }

  okFormulario(): void {
    const listaInput = ['nombre_usuario', 'login_usuario', 'clave_usuario', 'confirmar_clave_usuario', 'permisos'];
    for (const label of listaInput) {
      const from_group = document.getElementById('from_group_' + label) as HTMLInputElement || null;
      const text_select = document.getElementById('text_' + label) as HTMLInputElement || null;
      from_group.classList.remove('has-danger');
      text_select.innerText = '';
      from_group.classList.add('has-success');
    }
    const btn = document.getElementById('btn_form') as HTMLInputElement || null;
    btn.disabled = true;
  }

  completado(content: any) {
    this.okFormulario();
    setTimeout(() => {
      this.modalService.dismissAll('Cross click');
      this.ngOnInit();
      this.open(content);
      setTimeout(() => {
        this.modalService.dismissAll('Cross click');
      }, 1200);
    }, 800);
  }

  error401(error: any, mensaje: any): void {
    if (error.status === 401) {
      localStorage.clear();
      this.modalService.dismissAll('Cross click');
      this.mensaje = 'Sesión caducada';
      this.open(mensaje);
      this.router.navigate(['/login']).then(ex => null);
    }
  }

  mensajeModal(content: any, mensaje: string): void {
    this.open(content);
    this.mensaje = mensaje;
  }
}

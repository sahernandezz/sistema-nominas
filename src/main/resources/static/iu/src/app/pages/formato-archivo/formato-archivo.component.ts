import {Component, OnInit, ViewChild} from '@angular/core';
import {AppComponent} from '../../app.component';
import {FormularioArchivoHttp} from '../../shared/services/FormularioArchivo';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {Router} from '@angular/router';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-formato-archivo',
  templateUrl: './formato-archivo.component.html',
  styleUrls: ['./formato-archivo.component.scss']
})
export class FormatoArchivoComponent implements OnInit {
  mensaje: String = '';
  eventoFormato: Object = {estado: 'R', formato: null};
  toppings = new FormControl('');
  displayedColumns: string[] = ['Descricción', 'Acciones'];
  dataSource = new MatTableDataSource<FormatoArchivo>([]);
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(public modalService: NgbModal, private httpFormulario: FormularioArchivoHttp,
              public app: AppComponent, private router: Router) {
  }

  ngOnInit(): void {
    this.httpFormulario.listaFormularios().subscribe((data) => {
      this.dataSource.data = data;
    }, (error) => {
      if (error.status === 401) {
        localStorage.clear();
        this.router.navigate(['/login']).then(e => null);
      }
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
    this.eventoFormato = {estado: 'R'};
  }

  openUpdate(content: any, usuario: Usuario): void {
    this.open(content);
    this.eventoFormato = {estado: 'U', formato: usuario};
  }

  cambiarEstado(id: number, content: any, mensaje: any, content_resultado: any): void {
    this.open(content);
    this.httpFormulario.estadoFormulario(id).subscribe((data) => {
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

  selectInput(): string {
    const input = document.getElementById('descripcion') as HTMLInputElement || null;
    const from_group = document.getElementById('from_group_descripcion') as HTMLInputElement || null;
    const text = document.getElementById('text_descripcion') as HTMLInputElement || null;
    if (input.value === null || input.value === '') {
      from_group.classList.add('has-danger');
      text.innerText = 'Campo vació';
      return null;
    } else {
      from_group.classList.remove('has-danger');
      text.innerText = '';
      return input.value;
    }
  }

  registrarFormato = (e: Event, content: any, mensaje: any) => {
    e.preventDefault();
    const valueInput = this.selectInput();
    if (valueInput !== null) {
      this.httpFormulario.registrarFormulario({
        id: undefined,
        descripcion: valueInput,
        estado: undefined
      }).subscribe((data) => {
        this.completado(content);
      }, (error) => {
        this.error401(error, mensaje);
        this.mensajeModal(mensaje, error.error.message);
      });
    }
  }

  actualizarFormato(e: Event, content: any, mensaje: any): void {
    e.preventDefault();
    const valueInput = this.selectInput();
    if (valueInput !== null) {
      this.httpFormulario.actualizarFormulario({
          id: this.eventoFormato['formato']['id'],
          descripcion: valueInput,
          estado: this.eventoFormato['formato']['estado']
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
      const from_group = document.getElementById('from_group_descripcion') as HTMLInputElement || null;
      const text_select = document.getElementById('text_descripcion') as HTMLInputElement || null;
      from_group.classList.remove('has-danger');
      text_select.innerText = '';
      from_group.classList.add('has-success');
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
      this.open(mensaje);
      this.mensaje = 'Sesión caducada';
      localStorage.clear();
      this.router.navigate(['/login']).then(ex => null);
    }
  }

  mensajeModal(content: any, mensaje: string): void {
    this.open(content);
    this.mensaje = mensaje;
  }
}

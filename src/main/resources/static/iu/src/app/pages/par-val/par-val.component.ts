import {Component, OnInit, ViewChild} from '@angular/core';
import {AppComponent} from '../../app.component';
import {ParaValHttp} from '../../shared/services/ParaVal';
import {FormularioArchivoHttp} from '../../shared/services/FormularioArchivo';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {Router} from '@angular/router';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-par-val',
  templateUrl: './par-val.component.html',
  styleUrls: ['./par-val.component.scss']
})
export class ParValComponent implements OnInit {
  mensaje: String = '';
  eventoParVal: Object = {estado: 'R', parVal: null};
  formatos: FormatoArchivo[];
  toppings = new FormControl('');
  displayedColumns: string[] = ['Tipo', 'Celda', 'Columna', 'Tipo de dato', 'Valor permitido', 'Formato', 'Acciones'];
  listTipoDato: Object[] = [{valor: 'N', nombre: 'Numero'}, {valor: 'D', nombre: 'Fecha'}, {
    valor: 'S',
    nombre: 'Texto'
  }];
  listaTipo: string[] = ['ENCE', 'ENCD', 'DETE', 'DETD'];
  dataSource = new MatTableDataSource<ParaVal>([]);
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(public modalService: NgbModal, private httpParVal: ParaValHttp,
              private httpFormulario: FormularioArchivoHttp,
              public app: AppComponent, private router: Router) {
    this.formatos = this.getFormatos();
  }

  ngOnInit(): void {
    this.httpParVal.listaParVal().subscribe((data) => {
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
    this.eventoParVal = {estado: 'R'};
  }

  openUpdate(content: any, paraVal: ParaVal): void {
    this.open(content);
    this.eventoParVal = {estado: 'U', parVal: paraVal};
  }

  tipoDeDatoTabla(dato: string): string {
    let respuesta;
    this.listTipoDato.filter((item) => {
      if (item['valor'] === dato) {
        respuesta = item['nombre'];
      }
    });
    return respuesta;
  }

  cambiarEstado(id: number, content: any, mensaje: any, content_resultado: any): void {
    this.open(content);
    this.httpParVal.estadoParVal(id).subscribe((data) => {
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

  getFormatos(): FormatoArchivo[] {
    const lista = [];
    this.httpFormulario.listaFormulariosActivos().subscribe(data => {
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
    const listaInput = [
      {label: 'celda', isRequired: true},
      {label: 'columna', isRequired: false},
      {label: 'valorPer', isRequired: false},
      {label: 'expreSql', isRequired: false},
      {label: 'tipo', isRequired: true},
      {label: 'tipoDato', isRequired: true},
      {label: 'formatoArchivo', isRequired: true}
    ];

    for (const item of listaInput) {
      const input = document.getElementById(item.label) as HTMLInputElement || null;
      const from_group = document.getElementById('from_group_' + item.label) as HTMLInputElement || null;
      const text = document.getElementById('text_' + item.label) as HTMLInputElement || null;
      if (item.isRequired) {
        if (input.value === null || input.value === '' || input.value === 'null') {
          from_group.classList.add('has-danger');
          text.innerText = 'Campo vació';
        } else {
          i++;
          from_group.classList.remove('has-danger');
          ObjectResult[item.label] = input.value;
          text.innerText = '';
        }
      } else {
        i++;
        from_group.classList.remove('has-danger');
        ObjectResult[item.label] = input.value === '' ? undefined : input.value;
        text.innerText = '';
      }
    }
    return i === listaInput.length ? ObjectResult : null;
  }

  parse(formato: FormatoArchivo): string {
    return JSON.stringify(formato);
  }

  registrarParVal = (e: Event, content: any, mensaje: any) => {
    e.preventDefault();
    const valueInput = this.selectInput();
    if (valueInput !== null) {
      this.httpParVal.registrarParVal({
        id: undefined,
        tipo: valueInput['tipo'],
        celda: valueInput['celda'],
        columna: valueInput['columna'],
        tipoDato: valueInput['tipoDato'],
        valorPer: valueInput['valorPer'],
        expreSql: valueInput['expreSql'],
        formatoArchivo: JSON.parse(valueInput['formatoArchivo']),
        estado: undefined
      }).subscribe((data) => {
        this.completado(content);
      }, (error) => {
        this.error401(error, mensaje);
        this.mensajeModal(mensaje, error.error.message);
      });
    }
  }

  actualizarParVal(e: Event, content: any, mensaje: any): void {
    e.preventDefault();
    const valueInput = this.selectInput();
    if (valueInput !== null) {
      this.httpParVal.actualizarParVal({
          id: this.eventoParVal['parVal']['id'],
          tipo: valueInput['tipo'],
          celda: valueInput['celda'],
          columna: valueInput['columna'],
          tipoDato: valueInput['tipoDato'],
          valorPer: valueInput['valorPer'],
          expreSql: valueInput['expreSql'],
          formatoArchivo: JSON.parse(valueInput['formatoArchivo']),
          estado: this.eventoParVal['parVal']['estado']
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
    const listaInput = ['celda', 'columna', 'valorPer', 'expreSql', 'tipo', 'tipoDato', 'formatoArchivo'];
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

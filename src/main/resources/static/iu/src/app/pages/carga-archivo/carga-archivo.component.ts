import {Component, OnInit, ViewChild} from '@angular/core';
import {CargaArchivoHttp} from '../../shared/services/CargaArchivo';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormatoArchivoHttp} from '../../shared/services/FormatoArchivo';
import {Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-carga-archivo',
  templateUrl: './carga-archivo.component.html',
  styleUrls: ['./carga-archivo.component.scss']
})
export class CargaArchivoComponent implements OnInit {

  mensaje: String = '';
  file: any = null;
  tablaErrors: boolean = false;
  dataSource = new MatTableDataSource<ErrorArchivo>([]);
  informe: InformeCargarArchivo;
  listaFormatos: FormatoArchivo[];
  displayedColumns: string[] = ['Celda', 'Columna', 'Error'];
  estadoLoad: boolean;
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(public modalService: NgbModal,
              private httpCargaArchivo: CargaArchivoHttp,
              private httpFormatoArchivo: FormatoArchivoHttp,
              private router: Router) {
    this.estadoLoad = false;
    this.listaFormatos = this.getFormatos();
  }


  ngOnInit(): void {

  }

  refresh(): void {
    location.reload();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
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

  onFileSelected(e: any): void {
    this.file = e.target;
    this.tablaErrors = false;
  }

  subirArchivo(e: Event, content_mensaje: any): void {
    e.preventDefault();
    const btn = document.getElementById('btn-subir') as HTMLInputElement || null;
    const selectFormato = document.getElementById('formatoArchivo') as HTMLInputElement || null;
    if (this.file != null && selectFormato.value != 'null') {
      btn.disabled = true;
      this.httpCargaArchivo.guardarValidacionArchivo(this.file.files[0], JSON.parse(selectFormato.value)).subscribe((respuesta) => {
        this.file = null;
        btn.disabled = false;
        this.mensajeModal(content_mensaje, respuesta.message);
      }, (error) => {
        btn.disabled = false;
        this.error401(error, null);
        if (error.status === 400) {
          this.mensajeError(error, content_mensaje);
          if (error.error.object.listaErrores !== undefined) {
            this.dataSource.data = error.error.object.listaErrores;

            this.informe = {
              cantidadErrores: error.error.object.cantidadErrores,
              cantidadRegistros: error.error.object.cantidadRegistros,
              cantidadRegistrosValidados: error.error.object.cantidadRegistrosValidados
            };

            this.tablaErrors = true;
          }
        } else if (error.status === 500) {
          this.mensajeError(error, content_mensaje);
        }
      })
    } else {
      this.mensajeModal(content_mensaje,
        'Primero tiene que seleccionar un archivo y un formato valido');
    }
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

  private mensajeError(error: any, content_mensaje: any): void {
    this.file = null;
    this.mensaje = error.error.message;
    this.open(content_mensaje);
  }

  public getFormatos(): FormatoArchivo[] {
    const lista: FormatoArchivo[] = [];
    this.httpFormatoArchivo.listaFormulariosActivos().subscribe(data => {
      lista.push(...data);
    }, (error) => {
      if (error.status === 401) {
        localStorage.clear();
        this.router.navigate(['/login']).then(e => null);
      }
    });

    return lista;
  }

  parse(formato: FormatoArchivo): string {
    return JSON.stringify(formato);
  }
}

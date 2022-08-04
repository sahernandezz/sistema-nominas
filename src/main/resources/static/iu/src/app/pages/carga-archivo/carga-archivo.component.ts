import {Component, OnInit} from '@angular/core';
import {CargaArchivoHttp} from '../../shared/services/CargaArchivo';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";

@Component({
  selector: 'app-carga-archivo',
  templateUrl: './carga-archivo.component.html',
  styleUrls: ['./carga-archivo.component.scss']
})
export class CargaArchivoComponent implements OnInit {

  mensaje: String = '';
  file: any = null;

  constructor(public modalService: NgbModal,
              private httpCargaArchivo: CargaArchivoHttp,
              private router: Router) {
  }



  ngOnInit(): void {
  }

  open(content: any) {
    this.modalService.open(content, {centered: true, ariaLabelledBy: 'modal-basic-title', backdrop: 'static'});
  }

  onFileSelected(e: any): void {
    this.file = e.target;
  }

  subirArchivo(e: Event, content_mensaje: any): void {
    e.preventDefault();
    if (this.file != null) {
      this.httpCargaArchivo.guardarValidacionArchivo(this.file.files[0]).subscribe((respuesta) => {
        this.file = null;
        this.mensajeModal(content_mensaje, respuesta.message);
      }, (error) => {
        this.error401(error, null);
        if (error.status === 400 || error.status === 500) {
          this.mensaje = error.error.message;
          this.open(content_mensaje);
        }
      })
    } else {
      this.mensajeModal(content_mensaje, 'Primero tiene que seleccionar un archivo valido');
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
}

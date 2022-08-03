import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-carga-archivo',
  templateUrl: './carga-archivo.component.html',
  styleUrls: ['./carga-archivo.component.scss']
})
export class CargaArchivoComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  subirArchivo(e: Event): void {
    e.preventDefault();

  }

}

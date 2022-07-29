import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private readonly confing: Config;

  constructor() {
    this.confing = {
      title: 'App',
      url: 'http://localhost:8080',
    };
  }

  ngOnInit(): void {
  }

  public getConfig(): Config {
    return this.confing;
  }

  public setUsuario(usuario: Usuario) {
    localStorage.setItem('user', JSON.stringify(usuario));
  }

  public getUsuario(): Usuario {
    return JSON.parse(localStorage.getItem('user'));
  }
}

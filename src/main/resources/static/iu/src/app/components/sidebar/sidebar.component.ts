import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AppComponent} from '../../app.component';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  {path: '/usuarios', title: 'Usuarios', icon: 'ni-bullet-list-67 text-red', class: ''},
  {path: '/formatos', title: 'Formatos', icon: 'ni-bullet-list-67 text-red', class: ''},
  {path: '/parámetro-valor', title: 'Parámetro Valor', icon: 'ni-bullet-list-67 text-red', class: ''}
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = true;
  app: AppComponent;

  constructor(private router: Router) {
    this.app = new AppComponent();
  }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
    });
  }

  salir(): void {
    localStorage.clear();
    this.router.navigate(['/login']).then(e => null);
  }
}

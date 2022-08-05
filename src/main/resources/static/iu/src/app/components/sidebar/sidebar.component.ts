import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AppComponent} from '../../app.component';

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
    this.menuItems = this.app.getRoutes().filter(menuItem => menuItem);
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
    });
  }

  salir(): void {
    localStorage.clear();
    this.router.navigate(['/login']).then(e => null);
  }
}

import {Component, OnInit, ElementRef, HostListener} from '@angular/core';
import {ROUTES} from '../sidebar/sidebar.component';
import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {AppComponent} from '../../app.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  app: AppComponent;

  constructor(location: Location, private element: ElementRef, private router: Router) {
    this.location = location;
    this.app = new AppComponent();
  }

  ngOnInit() {
    this.listTitles = ROUTES.filter(listTitle => listTitle);
  }

  getTitle() {
    let titlee = this.location.prepareExternalUrl(this.location.path());
    if (titlee.charAt(0) === '#') {
      titlee = titlee.slice(1);
    }

    for (const item1 of this.listTitles) {
      if (item1.path === titlee) {
        return item1.title;
      }
    }
    return 'Perfil';
  }

  salir(): void {
    localStorage.clear();
    this.router.navigate(['/login']).then(e => null);
  }

}

import {Routes} from '@angular/router';

import {UserProfileComponent} from '../../pages/user-profile/user-profile.component';
import {UsuariosComponent} from '../../pages/usuarios/usuarios.component';
import {FormatoArchivoComponent} from '../../pages/formato-archivo/formato-archivo.component';
import {ParValComponent} from '../../pages/par-val/par-val.component';

export const AdminLayoutRoutes: Routes = [
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'usuarios', component: UsuariosComponent},
  {path: 'formatos', component: FormatoArchivoComponent},
  {path: 'parámetro-valor', component: ParValComponent}
];

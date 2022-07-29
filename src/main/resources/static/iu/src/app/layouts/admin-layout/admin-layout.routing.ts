import {Routes} from '@angular/router';

import {UserProfileComponent} from '../../pages/user-profile/user-profile.component';
import {UsuariosComponent} from '../../pages/usuarios/usuarios.component';

export const AdminLayoutRoutes: Routes = [
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'usuarios', component: UsuariosComponent}
];

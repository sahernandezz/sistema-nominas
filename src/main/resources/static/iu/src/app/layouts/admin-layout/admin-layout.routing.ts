import {Routes} from '@angular/router';

import {UserProfileComponent} from '../../pages/user-profile/user-profile.component';
import {UsuariosComponent} from '../../pages/usuarios/usuarios.component';
import {FormatoArchivoComponent} from '../../pages/formato-archivo/formato-archivo.component';
import {ParValComponent} from '../../pages/par-val/par-val.component';
import {CargaArchivoComponent} from '../../pages/carga-archivo/carga-archivo.component';

export const AdminLayoutRoutes: Routes = [
  {path: 'user-profile', component: UserProfileComponent},
  {path: 'usuarios', component: UsuariosComponent},
  {path: 'formatos', component: FormatoArchivoComponent},
  {path: 'par-valor', component: ParValComponent},
  {path: 'carga-archivo', component: CargaArchivoComponent}
];

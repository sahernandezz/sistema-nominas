interface Usuario {
  id: Number;
  login: string;
  clave: string;
  nombre: string;
  permisos: Rol[];
  estado: string;
}

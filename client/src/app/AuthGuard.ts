import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {JwtUtil} from "./util/JwtUtil"; // замените на путь к вашему сервису Jwt

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private jwtUtil: JwtUtil, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const role = this.jwtUtil.getRole();

    if (role === 'ADMIN') {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}

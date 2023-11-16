import {Injectable} from "@angular/core";
import {JwtHelperService} from "@auth0/angular-jwt";
import {TokenService} from "../services/TokenService";

@Injectable({
  providedIn: 'root'
})
export class JwtUtil {
  private jwtHelper: JwtHelperService;
  private token: string | any;

  constructor(private authService: TokenService) {
    this.jwtHelper = new JwtHelperService();
  }

  decodeToken() {
    this.authService.getToken().subscribe(value => {
      this.token = value
    });
    if (this.token !== null) {
      try {
        return this.jwtHelper.decodeToken(this.token);
      } catch (error) {
        console.error('Error decoding token:', error);
        return null;
      }
    }
  }

  getUsername(): string {
    return this.decodeToken().sub;
  }

  getRole(): string | null {
    if (this.token != null) {
      return this.decodeToken().role
    }
    return null;
  }

  getId(): number | undefined {
    if (this.token != null) {
      return Number.parseInt(this.decodeToken().id)
    }
    return 0
  }
}

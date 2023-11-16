import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {JwtUtil} from "../../util/JwtUtil";
import {TokenService} from "../../services/TokenService";

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  imports: [
    NgIf
  ],
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  accessToken: string | undefined;
  username: string | undefined;
  role: string | null | undefined;
  public userId: number | undefined;

  constructor(private router: Router,
              private jwtUtil: JwtUtil,
              private authService: TokenService) {
  }

  ngOnInit(): void {
    this.authService.getToken().subscribe(token => {
      this.accessToken = token;
      if (localStorage.getItem('accessToken') !== null) {
        this.username = this.jwtUtil.getUsername()
        this.userId = this.jwtUtil.getId();
      }
    });
    this.role = this.jwtUtil.getRole();

  }

  logout() {
    localStorage.removeItem("accessToken");
    this.goToPage('')
    location.reload();
  }

  public goToPage(pageName: string) {
    this.router.navigateByUrl(pageName)
  }
}

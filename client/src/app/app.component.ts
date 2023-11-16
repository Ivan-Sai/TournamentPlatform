import {Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {HeaderComponent} from "./layout/header/header.component";
import {JwtModule} from "@auth0/angular-jwt";

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    HeaderComponent,
    JwtModule,
  ],
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title = 'client';
}

import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [
    NgIf
  ]
})
export class HomeComponent implements OnInit {
  accessToken: string | null | undefined;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.accessToken = localStorage.getItem('accessToken')
  }


  public goToPage(pageName: string) {
    this.router.navigate([`${pageName}`])
  }
}

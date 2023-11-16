import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {TokenService} from "../../../../services/TokenService";
import {Location, NgIf} from "@angular/common";
import {AuthService} from "../../../../services/AuthService";
import {JwtUtil} from "../../../../util/JwtUtil";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit{
  constructor(private fb: FormBuilder,
              private service: AuthService,
              private router: Router,
              private authService: TokenService,
              private location: Location,
              private jwtUtil:JwtUtil
              ) {
  }

  ngOnInit(): void {
        if (this.jwtUtil.getId() != 0) this.router.navigateByUrl('')
    }

  errorMessage: string = ''

  public form: FormGroup = this.fb.group({
    email: new FormControl(null, [Validators.required, Validators.email]),
    password: new FormControl(null, [Validators.required]),
  });

  public goToPage(pageName: string) {
    this.router.navigate([`${pageName}`])
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.signIn(this.form.value).subscribe(responseData => {
          if (responseData && responseData.data['accessToken']) {
            this.authService.setToken(responseData.data['accessToken']);
            this.location.back()
            location.reload()
          }
        },
        error => {
          if (error.error) {
            this.errorMessage = error.error;
            console.log(this.errorMessage)
          }
        }
      );
    }
  }
}

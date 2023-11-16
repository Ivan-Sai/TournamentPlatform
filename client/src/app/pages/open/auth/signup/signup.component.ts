import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AsyncPipe, Location, NgIf} from "@angular/common";
import {Router} from "@angular/router";

import {TokenService} from "../../../../services/TokenService";
import {AuthService} from "../../../../services/AuthService";
import {JwtUtil} from "../../../../util/JwtUtil";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    AsyncPipe,
  ],
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  public passwordEquals: boolean | unknown | undefined;

  public token: string | undefined;

  public form: FormGroup = this.fb.group({
    email: new FormControl(null, [Validators.required, Validators.email]),
    username: new FormControl(null, [Validators.required]),
    password1: new FormControl(null, [Validators.required]),
    password2: new FormControl(null, [Validators.required]),
  });
  public errorMessage: string = '';

  constructor(private fb: FormBuilder,
              private service: AuthService,
              private router: Router,
              private authService: TokenService,
              private location: Location,
              private jwtUtil: JwtUtil,
  ) {
  }

  ngOnInit(): void {
    this.form.valueChanges.subscribe(value => {
      this.checkPasswordEquals(value['password1'], value['password2'])
    })
    if (this.jwtUtil.getId() != 0) this.router.navigateByUrl('')
  }

  onSubmit() {
    if (this.form.valid) {
      this.service.signup(this.form.value).subscribe(registerData => {
          if (registerData && registerData['accessToken']) {
            this.authService.setToken(registerData['accessToken']);
            this.location.back()
            location.reload()
          }
        },
        error => {
          if (error.error) {
            this.errorMessage = error.error;
          }
        }
      );
    }
  }
  public goToPage(pageName: string) {
    this.router.navigate([`${pageName}`])
  }
  private checkPasswordEquals(password1: string, password2: string) {
    if (password1 !== null && password2 !== null) {
      this.passwordEquals = password1 === password2;
    }
  }

}

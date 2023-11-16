import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {RegisterData} from "../models/request/RegisterData";
import {ResponseData} from "../models/ResponseData";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  public signIn(formValues: any): Observable<ResponseData<RegisterData>> {
    let email: string = formValues['email'];
    let password: string = formValues['password'];

    return this.http.post<ResponseData<RegisterData>>('http://localhost:8080/api/open/auth/login', {
      email, password
    })
  }

  public signup(formValues: any): Observable<RegisterData> {
    let email: string = formValues['email'];
    let username: string = formValues['username'];
    let password: string = formValues['password1'];

    return this.http.post<ResponseData<RegisterData>>('http://localhost:8080/api/open/auth/register', {
      username, email, password
    })
      .pipe(
        map(res => res.data)
      );
  }
}

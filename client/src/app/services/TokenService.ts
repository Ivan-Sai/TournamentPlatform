import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor() {
    const storedToken = localStorage.getItem('accessToken');
    if (storedToken) {
      this.tokenSubject.next(storedToken);
    }
  }

  setToken(token: string): void {
    localStorage.setItem('accessToken', token);
    this.tokenSubject.next(token);
  }

  getToken(): Observable<string> {
    return this.tokenSubject.asObservable();
  }


}

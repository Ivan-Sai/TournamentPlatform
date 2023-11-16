import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TournamentSignInService {
  private tournamentSignupWindowOpenSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public tournamentSignupWindowOpen$: Observable<boolean> = this.tournamentSignupWindowOpenSubject.asObservable();

  setTournamentSignupWindowOpen(value: boolean): void {
    this.tournamentSignupWindowOpenSubject.next(value);
  }
}

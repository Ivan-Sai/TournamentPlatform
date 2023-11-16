import {Component, OnDestroy, OnInit} from '@angular/core';
import {TournamentService} from "../../../../services/TournamentService";
import {ActivatedRoute, Router} from "@angular/router";
import {TournamentRequestData} from "../../../../models/request/TournamentRequestData";
import {AsyncPipe, DatePipe, LowerCasePipe, NgClass, NgForOf, NgIf, NgStyle} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {JwtUtil} from "../../../../util/JwtUtil";
import {interval, Observable, Subscription, take} from "rxjs";
import {TokenService} from "../../../../services/TokenService";
import {PersonalService} from "../../../../services/PersonalService";
import {TeamData} from "../../../../models/request/TeamData";
import {TournamentSignInComponent} from "../../../../shared/tournament-sign-in/tournament-sign-in.component";
import {TeamService} from "../../../../services/TeamService";
import {BracketComponent} from "./bracket/bracket.component";

@Component({
  selector: 'app-tournament',
  standalone: true,
  templateUrl: './tournament-page.component.html',
  imports: [
    NgIf,
    NgClass,
    FormsModule,
    NgStyle,
    DatePipe,
    LowerCasePipe,
    AsyncPipe,
    NgForOf,
    TournamentSignInComponent,
    BracketComponent
  ],
  styleUrls: ['./tournament-page.component.css']
})
export class TournamentPageComponent implements OnInit, OnDestroy {

  private subscription: Subscription = new Subscription();

  public infoLoaded: boolean = false;
  public tournamentId: number | undefined;
  public tournamentRequestData: TournamentRequestData | undefined;
  public teams$: Observable<TeamData[]> = this.personalService.getTeamsByPersonal(this.jwtUtil.getId());
  public userId = this.jwtUtil.getId();


  // @ts-ignore
  public uploadImage: File | undefined;
  public isHasImage: boolean = false;
  public dbImage: string | undefined;
  public role: string | null;
  public tournamentStart: Date | undefined;
  public timeUntilStart: number | undefined;
  public isStarted: boolean = false;
  public startDateString: string = '';

  public imageWindowOpen: boolean = false;
  public deleteWindowOpen: boolean = false;
  public cancelWindowOpen: boolean = false;
  public authWindowOpen: boolean = false;
  public tournamentSignupWindowOpen: boolean = false;
  public confirmTeamSignupWindowOpen: boolean = false;

  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: { [key: number]: boolean } = {}
  public chosenTeamId: number | undefined;
  public chosenTeamName: String | undefined;
  public hasEligibleTeams: boolean = false;
  public askToReload: boolean = false;

  constructor(private tournamentService: TournamentService,
              private route: ActivatedRoute,
              private router: Router,
              private jwtUtil: JwtUtil,
              private tokenService: TokenService,
              private personalService: PersonalService,
              private teamService: TeamService,
  ) {
    this.route.params.subscribe(value => {
      this.tournamentId = value['id']
    })
    this.role = this.jwtUtil.getRole();
  }


  ngOnInit(): void {
    this.tournamentService.getTournament(this.tournamentId).subscribe(data => {
        this.tournamentRequestData = data;
        this.infoLoaded = true;
        this.isStarted = data.isStarted;
        this.startDateString = new Date(data.startDate).toLocaleString()
        console.log(this.startDateString)
        console.log(this.tournamentRequestData)
        this.getTimeToStartTournament(data);
      },
      error => {
        if (error.error == "Tournament not found") {
          this.router.navigateByUrl('error/404')
        }
      });
    this.tournamentService.getTournamentLogo(this.tournamentId).subscribe(res => {
        this.dbImage = 'data:image/jpeg;base64,' + res.data;
        this.isHasImage = true;
      }
    )
    this.loadLogos();
    this.teams$.pipe(
      take(1),
    ).subscribe(
      teams => {
        for (let team of teams) {
          if (this.checkIsSignup(team.tournamentsId)) {
            this.hasEligibleTeams = true;
            break;
          }
        }
      }
    );
  }

  public onImageUpload(event: Event) {
    if (event && event.target) {
      // @ts-ignore
      this.uploadImage = event.target.files[0];
    }
  }

  public submitImage() {
    this.tournamentService.setTournamentImage(this.uploadImage, this.tournamentId).subscribe()
    location.reload();
  }

  public goToPage(pageName: string) {
    this.router.navigateByUrl(pageName)
  }

  private getTimeToStartTournament(data: TournamentRequestData) {
    if (data && data.startDate) {
      this.tournamentStart = new Date(data.startDate);
      this.subscription = interval(1000).subscribe(x => {
        const currentTime = new Date().getTime();
        if (this.tournamentStart) {
          this.timeUntilStart = this.tournamentStart.getTime() - currentTime;
          if (this.timeUntilStart <= 0 && !this.isStarted) {
            this.askToReload = true;
            console.log(this.tournamentRequestData?.bracketId)
          }
        }
      });
    }
  }

  formatDate(timeUntilStart: number | undefined) {
    if (timeUntilStart) {
      const days = Math.floor(timeUntilStart / (1000 * 60 * 60 * 24));
      const hours = Math.floor((timeUntilStart % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((timeUntilStart % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((timeUntilStart % (1000 * 60)) / 1000);

      let result = '';

      if (days > 0) {
        result += days + 'd ';
      }
      if (hours > 0) {
        result += hours + 'h ';
      }
      if (minutes > 0) {
        result += minutes + 'm ';
      }
      result += seconds + 's';

      return result.trim();
    }
    return undefined
  }


  public deleteTournament() {
    this.tournamentService.deleteTournament(this.tournamentId).subscribe(() =>
      this.router.navigateByUrl('tournaments')
    )
  }

  public cancelTournament() {
    this.tournamentService.cancelTournament(this.tournamentId).subscribe(() =>
        location.reload()
      , error => console.log(error.error))
  }

  public signUpTournament() {
    if (!localStorage.getItem('accessToken')) {
      this.authWindowOpen = true
    } else {
      this.tournamentSignupWindowOpen = true;
    }
  }

  loadLogos(): void {
    this.teams$.forEach(value => {
      value.forEach(value1 => {
        this.teamService.getTeamLogo(value1.id).subscribe(
          value2 => {
            this.logoDataMap[value1.id] = 'data:image/jpeg;base64,' + value2.data
            this.isHasLogoMap[value1.id] = true;
          }
          , error => {
            this.isHasLogoMap[value1.id] = false
          }
        )
      })
    })

  }

  getLogo(id: number): string {
    return this.logoDataMap[id]
  }

  ngOnDestroy(): void {
    if (this.subscription)
      this.subscription.unsubscribe();
  }


  confirmTeamSignupWindow(id: number, name: String) {
    this.tournamentSignupWindowOpen = false;
    this.confirmTeamSignupWindowOpen = true;
    this.chosenTeamId = id;
    this.chosenTeamName = name;
  }

  backToSignUpWindow() {
    this.tournamentSignupWindowOpen = true;
    this.confirmTeamSignupWindowOpen = false;
    this.chosenTeamId = undefined;
    this.chosenTeamName = undefined;
  }

  confirmSignup() {
    this.tournamentService.addTeam(this.tournamentId, this.chosenTeamId).subscribe(
      () => {
        this.confirmTeamSignupWindowOpen = false;
        this.chosenTeamId = undefined;
        this.chosenTeamName = undefined;
        location.reload();
      }
    );
  }

  checkIsSignup(tournamentsIds: number[]) {
    if (this.tournamentId) {
      for (let tournamentsId of tournamentsIds) {
        if (tournamentsId == this.tournamentId) return true;
      }
    }
    return false;
  }
}

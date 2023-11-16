import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TeamService} from "../../../../services/TeamService";
import {JwtUtil} from "../../../../util/JwtUtil";
import {Router} from "@angular/router";
import {Location} from "@angular/common";

@Component({
  selector: 'app-team-creation',
  standalone: true,
  templateUrl: './team-creation.component.html',
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['./team-creation.component.css']
})
export class TeamCreationComponent implements OnInit{

  uploadImage :File | undefined;

  constructor(private fb:FormBuilder,
              private teamService: TeamService,
              private jwtUtil:JwtUtil,
              private router: Router,
              private location:Location
  ) {
  }

  ngOnInit(): void {
        if (this.jwtUtil.getRole() == null || this.jwtUtil.getRole() != 'PERSONAL'){
          this.router.navigateByUrl('signin')
        }
    }

  public form: FormGroup = this.fb.group({
    name: new FormControl(null, [Validators.required,Validators.minLength(2),Validators.maxLength(12)]),
    image: new FormControl(null, [Validators.required]),
  });

  public onSubmit() {
    if (this.form.valid) {
      this.teamService.createTeam(this.form.value.name,this.uploadImage).subscribe(value => {
        this.router.navigateByUrl('teams/'+value)
        }
      )
    }
  }
  public onImageUpload(event:Event) {
    if (event && event.target){
      // @ts-ignore
      this.uploadImage = event.target.files[0];
    }
  }

  redirectToPage() {
    this.location.back()
  }
}

feature 'Creating and viewing a post' do

    before(:all) do
        visit '/'
        within(class: 'signup') do
            fill_in "name", with: 'user'
            fill_in "email", with: "email@me.com"
            fill_in "password", with: "password123"
            click_button 'Submit'
        end
    end

    scenario 'User can view post on posts page' do
        visit '/posts'
        expect(page).to have_content /test message body/
   end


   scenario 'User can create and view post on posts page' do
           visit '/posts'
           fill_in :post, with: "This is another test post"
           click_button "Submit"
           expect(page).to have_content /This is another test post/
      end
end
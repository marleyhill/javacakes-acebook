feature 'Creating and viewing a post' do
    scenario 'User can view post on posts page' do
        visit '/posts'
        expect(page).to have_content "This is a test post"
   end


# this is a failing test as not funmctionality to add new posts on website yet
   scenario 'User can create and view post on posts page' do
           visit '/posts'
           fill_in :post, with: "This is a new test post"
           click_button "Submit"
           expect(page).to have_content "This is a new test post"
      end
end